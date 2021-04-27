package com.treeleaf.januswebrtc;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

import static com.treeleaf.januswebrtc.Const.API_SECRET;
import static com.treeleaf.januswebrtc.Const.CLIENT;
import static com.treeleaf.januswebrtc.Const.getRoomNumber;
import static com.treeleaf.januswebrtc.VideoCallUtil.generateTransactionString;


public class RestChannel implements ApiHandlerCallback {
    private static final String TAG = "WebSocketChannel";

    private ConcurrentHashMap<String, JanusTransaction> transactions = new ConcurrentHashMap<>();
    private ConcurrentHashMap<BigInteger, JanusHandle> handles = new ConcurrentHashMap<>();
    private ConcurrentHashMap<BigInteger, JanusHandle> feeds = new ConcurrentHashMap<>();
    private BigInteger mSessionId, mRoomId, mParticipantId;
    private Callback.JanusRTCInterface delegate;
    private Callback.ApiCallback apiCallback;
    ApiHandler apiHandler;
    private BigInteger publisherHandleId;
    private Role role;

    public RestChannel(Context context, String janusServerUrl, String apiKey, String apiSecret) {
        apiHandler = new ApiHandler(context, this, janusServerUrl, apiKey);
        API_SECRET = apiSecret;
    }

    public void initConnection(String client) {
        if (client.equals(CLIENT)) {
            createSession();
            role = Role.CLIENT;
        } else {
            createSession();
            role = Role.SERVER;
        }
    }

    @Override
    public void removeTransaction(String transactionId) {
        transactions.remove(transactionId);
    }

    @Override
    public void onReceivePollEvent(String result) {
        RestChannel.this.onMessage(result);
        Log.d(TAG, "onReceivePollEvent");
    }

    @Override
    public void restError(String message) {
        apiCallback.restError(message);
    }

    @Override
    public void updateProgressMessage(String message) {
        apiCallback.updateProgressMessage(message);
    }

    private void onMessage(String message) {
        Log.e(TAG, "onMessage" + message);
        try {
            JSONObject jo = new JSONObject(message);
            String janus = jo.optString("janus");
            if (janus.equals("keepalive")) {
                apiHandler.pollRequest(mSessionId);
            } else {
                if (jo.has("janus") || janus.equals("event")) {
                    apiHandler.pollRequest(mSessionId);
                }
                if (janus.equals("success")) {
                    String transaction = jo.optString("transaction");
                    JanusTransaction jt = transactions.get(transaction);
                    if (jt.success != null) {
                        jt.success.success(jo);
                    }
                    transactions.remove(transaction);
                } else if (janus.equals("error")) {
                    String transaction = jo.optString("transaction");
                    JanusTransaction jt = transactions.get(transaction);
                    if (jt.error != null) {
                        jt.error.error(jo);
                    }
                    transactions.remove(transaction);
                    apiCallback.restError("error occured!!!");
                } else if (janus.equals("ack")) {
                    Log.e(TAG, "Just an ack");
                } else if (janus.equals("slowlink")) {
                    String senderHandleId = jo.optString("sender");
                    apiCallback.onSlowLink(new BigInteger(senderHandleId));
                    Log.e(TAG, "slowlink");
                } else if (janus.equals("hangup")) {
                    /**
                     *  - this might end up when connection is closed from remote end
                     *  -
                     */
                    Log.d(TAG, "hangup occured");
                    String reasonForHangup = jo.optString("reason");
//                    apiCallback.onHangUp();
                } else if (janus.equals("media")) {
                    if (jo.has("type") && jo.optString("type").equals("video")) {
                        apiCallback.onVideoRendered();
                    }
                } else {
                    JanusHandle handle = handles.get(new BigInteger(jo.optString("sender")));
                    if (handle == null) {
                        Log.e(TAG, "missing handle");
                    } else if (janus.equals("event")) {
                        JSONObject plugin = jo.optJSONObject("plugindata").optJSONObject("data");
                        if (plugin.optString("videoroom").equals("joined")) {
                            mParticipantId = new BigInteger(plugin.optString("id"));
                            if (handle.onJoined != null)
                                handle.onJoined.onJoined(handle);


                            //all of these publishers are already active so no need to check for active
                            JSONArray publishers = plugin.optJSONArray("publishers");
                            if (publishers != null && publishers.length() > 0) {
                                for (int i = 0, size = publishers.length(); i <= size - 1; i++) {
                                    JSONObject publisher = publishers.optJSONObject(i);
                                    BigInteger feed = new BigInteger(publisher.optString("id"));
                                    apiCallback.onRoomJoined(getRoomNumber(), publisher.optString("id"));
                                }
                            } else
                                apiCallback.onActivePublisherNotFound();

                        }

                        /**
                         * when some one active new publisher joins the room
                         */
                        if (plugin.optString("videoroom").equals("event")) {
                            JSONArray publishers = plugin.optJSONArray("publishers");
                            if (publishers != null && publishers.length() > 0) {
                                for (int i = 0, size = publishers.length(); i <= size - 1; i++) {
                                    JSONObject publisher = publishers.optJSONObject(i);
                                    BigInteger feed = new BigInteger(publisher.optString("id"));
                                    String display = publisher.optString("display");
                                    apiCallback.onRoomJoined(apiCallback.getRoomNumber(), feed.toString());
                                }
                            }
                        }

                        String configured = plugin.optString("configured");
                        if (!TextUtils.isEmpty(configured) && configured.equals("ok")) {
                            apiCallback.onParticipantCreated(mParticipantId);
                            apiCallback.janusServerConfigurationSuccess(mSessionId, mRoomId, mParticipantId);
//                            apiCallback.showVideoCallStartView(false);
                            apiCallback.onPublisherVideoStarted();
                        }

                        String started = plugin.optString("started");
                        if (!TextUtils.isEmpty(started) && started.equals("ok")) {
                            apiCallback.showVideoCallStartView(false);
                            apiCallback.handleVideoCallViewForSingleCall();
                        }

                        //publisher has left and other participants will get following event
                        /**
                         * {
                         *         "videoroom" : "event",
                         *         "room" : <room ID>,
                         *         "leaving|unpublished" : <unique ID of the publisher who left>
                         * }
                         */
                        String leaving = plugin.optString("leaving");
                        if (!TextUtils.isEmpty(leaving)) {
                            JanusHandle jhandle = feeds.get(new BigInteger(leaving));
                            jhandle.onLeaving.onJoined(jhandle);
                            apiCallback.onParticipantLeftJanus(leaving);
                        }

                        //this event occurs after publisher has unpublished the stream
                        String unpublished = plugin.optString("unpublished");
                        if (!TextUtils.isEmpty(unpublished) && unpublished.equals("ok")) {
                            //gets called on publisher side
                        }

                        String room = plugin.optString("room");
                        if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(unpublished)) {
                            //gets called on subscriber side
                            BigInteger roomId = new BigInteger(room);//room id
                            BigInteger publisherId = new BigInteger(unpublished);//publisher's id who unpublished
                            apiCallback.streamUnpublished(roomId, publisherId);
                        }

                        JSONObject jsep = jo.optJSONObject("jsep");
                        if (jsep != null) {
                            handle.onRemoteJsep.onRemoteJsep(handle, jsep);
                        }

                    } else if (janus.equals("detached")) {
                        handle.onLeaving.onJoined(handle);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createSession() {
        String transaction = generateTransactionString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success = new TransactionCallbackSuccess() {
            @Override
            public void success(JSONObject jo) {
                mSessionId = new BigInteger(jo.optJSONObject("data").optString("id"));
                apiHandler.pollRequest(mSessionId);
                publisherCreateHandle();//TODO: uncomment this later
            }
        };
        jt.error = new TransactionCallbackError() {
            @Override
            public void error(JSONObject jo) {
                JSONObject error = jo.optJSONObject("error");
                String reason = error.optString("reason");
                apiCallback.restError(reason);
            }
        };
        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "create");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("transaction", transaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.createSession(msg.toString(), jt);
    }

    public void publisherCreateHandle() {
        String transaction = generateTransactionString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success = new TransactionCallbackSuccess() {
            @Override
            public void success(JSONObject jo) {
                BigInteger handleId = new BigInteger(jo.optJSONObject("data").optString("id"));
                publisherHandleId = handleId;
                if (role == Role.CLIENT) {
                    /**
                     * client has to create room it self
                     * and server just has to join
                     */
                    publisherCreateRoom(handleId);
                } else {
                    apiCallback.startCreatingOffer(publisherHandleId);
                    JanusHandle janusHandle = new JanusHandle();
                    janusHandle.handleId = publisherHandleId;
                    janusHandle.onJoined = new OnJoined() {
                        @Override
                        public void onJoined(JanusHandle jh) {
                            /**
                             * gets called when publihsher is joined
                             */
                            apiCallback.onRoomCreated(getRoomNumber());
//                            apiCallback.onRoomJoined(getRoomNumber(), null);
                        }
                    };
                    janusHandle.onRemoteJsep = new OnRemoteJsep() {
                        /**
                         * janus server's response to "publish" or "confiure" request
                         * and it is accompanied by JSEP SDP offer describing the publisher's media streams
                         * @param jh
                         * @param jsep
                         */

                        @Override
                        public void onRemoteJsep(JanusHandle jh, JSONObject jsep) {
                            /**
                             * gets called when
                             * {"videoroom": "event",
                             *       "room": 1234,
                             *       "configured": "ok",} this event is sent from server
                             *       this event also sends jsep object from server
                             */
                            delegate.onPublisherRemoteJsep(jh.handleId, jsep);
                        }
                    };
                    handles.put(janusHandle.handleId, janusHandle);
                    publisherJoinRoom(janusHandle);
                }
            }
        };
        jt.error = new TransactionCallbackError() {
            @Override
            public void error(JSONObject jo) {
            }
        };
        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "attach");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("plugin", "janus.plugin.videoroom");
            msg.putOpt("transaction", transaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.publisherCreateHandle(msg.toString(), jt, mSessionId);
    }

    public void destroySession() {
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "destroy");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("transaction", generateTransactionString(12));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSessionId != null && publisherHandleId != null)
            apiHandler.destroySession(msg.toString(), mSessionId);
    }

    private void publisherCreateRoom(BigInteger handleId) {
        String transaction = generateTransactionString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success = new TransactionCallbackSuccess() {
            @Override
            public void success(JSONObject jo) {
                /**
                 * save room number
                 */
                BigInteger roomNumber = new BigInteger(jo.optJSONObject("plugindata").optJSONObject("data").optString("room"));
                mRoomId = roomNumber;
                Const.setRoomNumber(roomNumber);

                //start creating offer right after room is created
                apiCallback.startCreatingOffer(publisherHandleId);

                JanusHandle janusHandle = new JanusHandle();
                janusHandle.handleId = new BigInteger(jo.optString("sender"));
                janusHandle.onJoined = new OnJoined() {
                    @Override
                    public void onJoined(JanusHandle jh) {
                        /**
                         * gets called when publihsher is joined
                         */
                        apiCallback.onRoomCreated(getRoomNumber());
//                        delegate.onPublisherJoined(jh.handleId);
                    }
                };
                janusHandle.onRemoteJsep = new OnRemoteJsep() {
                    /**
                     * janus server's response to "publish" or "confiure" request
                     * and it is accompanied by JSEP SDP offer describing the publisher's media streams
                     * @param jh
                     * @param jsep
                     */

                    @Override
                    public void onRemoteJsep(JanusHandle jh, JSONObject jsep) {
                        /**
                         * gets called when
                         * {"videoroom": "event",
                         *       "room": 1234,
                         *       "configured": "ok",} this event is sent from server
                         *       this event also sends jsep object from server
                         */
                        delegate.onPublisherRemoteJsep(jh.handleId, jsep);
                    }
                };
                handles.put(janusHandle.handleId, janusHandle);
                publisherJoinRoom(janusHandle);//asynchronous server response
            }
        };
        jt.error = new TransactionCallbackError() {
            @Override
            public void error(JSONObject jo) {
            }
        };
        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "create");
            body.putOpt("is_private", true);

            msg.putOpt("janus", "message");
            msg.putOpt("plugin", "janus.plugin.videoroom");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("body", body);
            msg.putOpt("transaction", transaction);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.publisherCreateRoom(msg.toString(), jt, handleId, mSessionId);
    }

    private void publisherJoinRoom(JanusHandle handle) {
        Log.d("callerroomnumber", "room number: " + getRoomNumber());
        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "join");
            body.putOpt("room", getRoomNumber());
            body.putOpt("ptype", "publisher");
            body.putOpt("display", "Android webrtc");

            msg.putOpt("janus", "message");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("body", body);
            msg.putOpt("transaction", generateTransactionString(12));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.publisherJoinRoom(msg.toString(), handle, mSessionId);
    }

    public void publisherCreateOffer(final BigInteger handleId, final SessionDescription sdp) {
        JSONObject publish = new JSONObject();
        JSONObject jsep = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            publish.putOpt("request", "configure");
            publish.putOpt("audio", true);
            publish.putOpt("video", true);

            jsep.putOpt("type", sdp.type);
            jsep.putOpt("sdp", sdp.description);

            message.putOpt("janus", "message");
            message.putOpt("apisecret", API_SECRET);
            message.putOpt("body", publish);
            message.putOpt("jsep", jsep);
            message.putOpt("transaction", generateTransactionString(12));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.publisherCreateOffer(message.toString(), handleId, mSessionId);
    }

    public void publisherUnpublish() {
        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "unpublish");//or you can put "leave"
            body.putOpt("room", getRoomNumber());

            msg.putOpt("janus", "message");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("body", body);
            msg.putOpt("transaction", generateTransactionString(12));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSessionId != null && publisherHandleId != null)
            apiHandler.unpublishRoom(msg.toString(), publisherHandleId, mSessionId);
    }

    public void subscriberLeave() {
        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "leave");//or you can put "leave"
            body.putOpt("room", getRoomNumber());

            msg.putOpt("janus", "message");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("body", body);
            msg.putOpt("transaction", generateTransactionString(12));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSessionId != null && publisherHandleId != null)
            apiHandler.unpublishRoom(msg.toString(), publisherHandleId, mSessionId);
    }

    public void detachPlugin() {
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "detach");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("transaction", generateTransactionString(12));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSessionId != null && publisherHandleId != null)
            apiHandler.detachPlugin(msg.toString(), publisherHandleId, mSessionId);
    }


    public void subscriberCreateAnswer(final BigInteger handleId, final SessionDescription sdp) {
        JSONObject body = new JSONObject();
        JSONObject jsep = new JSONObject();
        JSONObject message = new JSONObject();

        try {
            body.putOpt("request", "start");
            body.putOpt("room", getRoomNumber());

            jsep.putOpt("type", sdp.type);
            jsep.putOpt("sdp", sdp.description);
            message.putOpt("janus", "message");
            message.putOpt("apisecret", API_SECRET);
            message.putOpt("body", body);
            message.putOpt("jsep", jsep);
            message.putOpt("transaction", generateTransactionString(12));
            Log.e(TAG, "-------------" + message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.subscriberCreateAnswer(message.toString(), handleId, mSessionId);
    }

    public void trickleCandidate(final BigInteger handleId, final IceCandidate iceCandidate) {
        JSONObject candidate = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            candidate.putOpt("candidate", iceCandidate.sdp);
            candidate.putOpt("sdpMid", iceCandidate.sdpMid);
            candidate.putOpt("sdpMLineIndex", iceCandidate.sdpMLineIndex);

            message.putOpt("janus", "trickle");
            message.putOpt("apisecret", API_SECRET);
            message.putOpt("candidate", candidate);
            message.putOpt("transaction", generateTransactionString(12));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.trickleCandidate(message.toString(), handleId, mSessionId);
    }

    public void trickleCandidateComplete(final BigInteger handleId) {
        JSONObject candidate = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            candidate.putOpt("completed", true);

            message.putOpt("janus", "trickle");
            message.putOpt("apisecret", API_SECRET);
            message.putOpt("candidate", candidate);
            message.putOpt("transaction", generateTransactionString(12));
            apiHandler.trickleCandidateComplete(message.toString(), handleId, mSessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void subscriberCreateHandle(String participantId) {
        String transaction = generateTransactionString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success = new TransactionCallbackSuccess() {
            @Override
            public void success(JSONObject jo) {
                BigInteger handleId = new BigInteger(jo.optJSONObject("data").optString("id"));
                subscriberJoinRoom(handleId, participantId);
            }
        };
        jt.error = new TransactionCallbackError() {
            @Override
            public void error(JSONObject jo) {
            }
        };

        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "attach");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("plugin", "janus.plugin.videoroom");
            msg.putOpt("transaction", transaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.subscriberCreateHandle(msg.toString(), jt, mSessionId);
    }

    private void subscriberJoinRoom(final BigInteger handleId, String participantId) {
        JanusHandle janusHandle = new JanusHandle();
        janusHandle.handleId = handleId;
        janusHandle.feedId = new BigInteger(participantId);
        janusHandle.onRemoteJsep = new OnRemoteJsep() {
            @Override
            public void onRemoteJsep(JanusHandle jh, JSONObject jsep) {
                /**
                 * gets called when subcriber is joined
                 */
                delegate.subscriberHandleRemoteJsep(jh.handleId, jsep);
            }
        };
        janusHandle.onJoined = new OnJoined() {
            @Override
            public void onJoined(JanusHandle jh) {
                apiCallback.startCreatingOffer(handleId);
            }
        };

        janusHandle.onLeaving = new OnJoined() {
            @Override
            public void onJoined(JanusHandle jh) {
                subscriberOnLeaving(jh);
            }
        };
        handles.put(janusHandle.handleId, janusHandle);
        feeds.put(janusHandle.feedId, janusHandle);
        subscriberJoinRoom(janusHandle);//asynchronous server response
    }

    private void subscriberJoinRoom(JanusHandle handle) {

        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "join");
            body.putOpt("room", getRoomNumber());
            body.putOpt("ptype", "subscriber");
            body.putOpt("feed", handle.feedId);//unique ID of the publisher to subscribe to; mandatory

            msg.putOpt("janus", "message");
            msg.putOpt("apisecret", API_SECRET);
            msg.putOpt("body", body);
            msg.putOpt("transaction", generateTransactionString(12));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiHandler.subscriberJoinRoom(msg.toString(), handle, mSessionId);
    }

    private void subscriberOnLeaving(final JanusHandle handle) {
        String transaction = generateTransactionString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success = new TransactionCallbackSuccess() {
            @Override
            public void success(JSONObject jo) {
                delegate.onLeaving(handle.handleId);
                handles.remove(handle.handleId);
                feeds.remove(handle.feedId);
            }
        };
        jt.error = new TransactionCallbackError() {
            @Override
            public void error(JSONObject jo) {
            }
        };

        transactions.put(transaction, jt);

        /**
         * follwing request destroys the plugin handle,
         * this will also destroy the endpoint created for this plugin handle.
         */

        JSONObject jo = new JSONObject();
        try {
            jo.putOpt("janus", "detach");
            jo.putOpt("apisecret", API_SECRET);
            jo.putOpt("transaction", transaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDelegate(Callback.JanusRTCInterface delegate) {
        this.delegate = delegate;
    }

    public void setApiCallback(Callback.ApiCallback apiCallback) {
        this.apiCallback = apiCallback;
    }

    public void clearPendingApiCalls() {
        if (apiHandler != null)
            apiHandler.clearDisposables();
    }

    public enum Role {
        CLIENT, SERVER
    }

}
