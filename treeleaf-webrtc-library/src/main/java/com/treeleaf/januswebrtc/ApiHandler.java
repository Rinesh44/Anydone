package com.treeleaf.januswebrtc;

import android.content.Context;
import android.util.Log;

import com.treeleaf.januswebrtc.rest.ApiClient;
import com.treeleaf.januswebrtc.rest.ApiService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.treeleaf.januswebrtc.Const.API_SECRET;

public class ApiHandler {

    public static final String TAG = "ApiHandler";
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonParser jsonParser;
    private ApiHandlerCallback _mApiCallback;

    public ApiHandler(Context context, ApiHandlerCallback apiHandlerCallback,
                      String janusServerUrl, String apiKey) {
        this.apiService = ApiClient.getClient(context, VideoCallUtil.appendEndPoint(janusServerUrl),
                apiKey).create(ApiService.class);
        _mApiCallback = apiHandlerCallback;
        jsonParser = new JsonParser();
    }

    public void pollRequest(final BigInteger mSessionId) {
        disposable.add(
                apiService.longPollReq(mSessionId, API_SECRET)
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement response) {
                                Log.d(TAG, "longPollOnNext ");
                                Log.d(TAG, "-------" + response.toString());
                                _mApiCallback.onReceivePollEvent(response.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "longpollonError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "longpollonComplete ");
                            }
                        })
        );
    }

    public void createSession(String json, final JanusTransaction jt) {
        _mApiCallback.updateProgressMessage("Creating session...");
        disposable.add(
                apiService.createSession(convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("success")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.success != null) {
                                            jt.success.success(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    } else if (janus.equals("error")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.error != null) {
                                            jt.error.error(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void publisherCreateHandle(String json, final JanusTransaction jt, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Creating handle...");
        disposable.add(
                apiService.attachVideoPlugin(mSessionId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("success")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.success != null) {
                                            jt.success.success(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    } else if (janus.equals("error")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.error != null) {
                                            jt.error.error(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void subscriberCreateHandle(String json, final JanusTransaction jt, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Creating handle... ");
        disposable.add(
                apiService.attachVideoPlugin(mSessionId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("success")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.success != null) {
                                            jt.success.success(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    } else if (janus.equals("error")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.error != null) {
                                            jt.error.error(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void publisherCreateRoom(String json, final JanusTransaction jt, BigInteger handleId, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Creating room... ");
        disposable.add(
                apiService.createVideoRoom(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("success")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.success != null) {
                                            jt.success.success(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    } else if (janus.equals("error")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.error != null) {
                                            jt.error.error(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );

    }


    //synchronous
    public void listParticipants(String json, final JanusTransaction jt, BigInteger mSessionId, BigInteger handleId) {
        _mApiCallback.updateProgressMessage("Finding participants... ");
        disposable.add(
                apiService.listParticipants(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("success")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.success != null) {
                                            jt.success.success(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    } else if (janus.equals("error")) {
                                        String transaction = jo.optString("transaction");
                                        if (jt.error != null) {
                                            jt.error.error(jo);
                                        }
                                        _mApiCallback.removeTransaction(transaction);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    private JsonObject convertStringToJson(String jsonStr) {
        return jsonParser.parse(jsonStr).getAsJsonObject();
    }

    public void publisherJoinRoom(String json, JanusHandle handle, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Joining room... ");
        disposable.add(
                apiService.joinVideoRoom(mSessionId, handle.handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void unpublishRoom(String json, BigInteger handleId, BigInteger mSessionId) {
        disposable.add(
                apiService.unPublish(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void detachPlugin(String json, BigInteger handleId, BigInteger mSessionId) {
        disposable.add(
                apiService.detachPlugin(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void destroySession(String json, BigInteger mSessionId) {
        disposable.add(
                apiService.destroySession(mSessionId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void subscriberJoinRoom(String json, JanusHandle handle, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Joining room... ");
        disposable.add(
                apiService.joinVideoRoom(mSessionId, handle.handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void publisherCreateOffer(String json, BigInteger handleId, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Creating offer... ");
        disposable.add(
                apiService.publishVideo(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void subscriberCreateAnswer(String json, BigInteger handleId, BigInteger mSessionId) {
        _mApiCallback.updateProgressMessage("Craeting answer... ");
        disposable.add(
                apiService.startVideo(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void trickleCandidate(String json, BigInteger handleId, BigInteger mSessionId) {
        disposable.add(
                apiService.startVideo(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void trickleCandidateComplete(String json, BigInteger handleId, BigInteger mSessionId) {
        disposable.add(
                apiService.startVideo(mSessionId, handleId, convertStringToJson(json))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableObserver<JsonElement>() {
                            @Override
                            public void onNext(JsonElement message) {
                                Log.d(TAG, "-------" + message.toString());
                                try {
                                    JSONObject jo = new JSONObject(message.toString());
                                    String janus = jo.optString("janus");
                                    if (janus.equals("ack")) {
                                        Log.e(TAG, "Just an ack --- " + message.toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onError(new Throwable(e.getMessage()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError " + e.toString());
                                _mApiCallback.restError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete ");
                            }
                        })
        );
    }

    public void clearDisposables() {
        disposable.dispose();
    }

}
