# Janus Webrtc Library


### After importing this library module to your project,

* Add following line in project level gradle of your root project.
```groovy
allprojects {
    repositories {
        google()
        jcenter()
        flatDir { //add this line
            dirs 'libs'
        }
        //and this line
        maven { url 'https://jitpack.io' }

    }
}
```
* You should have this library appeared on settings.gradle.
```groovy
include ':treeleaf-webrtc-library'
```

* Add following line in your app level module.

```groovy
repositories {
    flatDir {
        dirs 'libs', '../treeleaf-webrtc-library/libs'
    }
}
```
* Add following inside your dependency list.
```groovy
implementation project(":treeleaf-webrtc-library")
```

* Add following line in your app level module.

```groovy
compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

### Usage

```java
MainActivity.java

public class MainActivity extends AppCompatActivity {

    private ClientActivity.OnVideoCallJoineeReceived videoCallListener;
    private Callback.HostActivityCallback hostActivityCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClientActivity.launch(MainActivity.this, true, "http://mediaserver.anydone.net/janus",
                "1234", "anydone@321123!@#", "John Cena",
                "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.outstandingcolleges.com%2Fwp-content%2Fuploads%2F2015%2F03%2FMichael-Schoendorf.jpg&f=1&nofb=1");
        //or

        hostActivityCallback = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {
                presenter.fetchJanusServerUrl(Hawk.get(Constants.TOKEN));
            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {
                //you will receive janus servers sessionid, roomid and participantid here
            }

            @Override
            public void passJoineeReceivedCallback(ClientActivity.OnVideoCallJoineeReceived callback) {
                videoCallListener = callback;
                //videoCallListener this callback object is optional
                //and is for notifying new participant joined in ClientActivity screen.
            }

            @Override
            public void notifyHostHangUp() {
                presenter.publishHostHangUpEvent(accountId, accountName, accountPicture,
                        serviceRequestId, rtcMessageId, videoBroadCastPublish);
            }
        };

        ClientActivity.launch(MainActivity.this, false, hostActivityCallback,
                serviceName, serviceProfileUri);
    }


    public void onVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
        if (videoCallListener != null) {
            UserProto.Account account = videoCallJoinResponse.getSenderAccount();
            videoCallListener.joineeReceived(account.getFullName(), account.getProfilePic());
            //notify new joined participant in ClientActivity
        }
    }

    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        Log.d(MQTT, "onParticipantLeft");
        if (videoCallListener != null) {
            UserProto.Account account = participantLeft.getSenderAccount();
            videoCallListener.onJoineeRemoved(account.getAccountId());
        }
    }


    @Override
    public void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret) {
        this.janusBaseUrl = janusBaseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        Log.d(TAG, "janus server info: " + janusBaseUrl + apiKey + apiSecret);
        videoCallListener.onJanusCredentialsReceived(janusBaseUrl, apiKey,
                apiSecret, serviceName, serviceProfileUri);
    }

    @Override
    public void onUrlFetchFail(String msg) {
        videoCallListener.onJanusCredentialsFailure();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServiceRequestDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}


```