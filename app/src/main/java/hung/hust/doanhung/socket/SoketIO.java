package hung.hust.doanhung.socket;

import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class SoketIO {
    String uri ="";
    private static Socket mSocket;
    {
        if(mSocket == null){
            try{
            mSocket = IO.socket(uri);
        }catch (URISyntaxException e){

            }
        }
    }

    public SoketIO() {
        if(getmSocket()!=null){
            mSocket.connect();
        }
    }

    public static Socket getmSocket() {
        return mSocket;
    }
    public void socketSendGPS(JSONObject data){
        mSocket.emit("",data.toString());
    }

}
