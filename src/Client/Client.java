package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client {

    private final Socket socket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public byte[] fileReceiver() throws Exception{

        byte[] buffer = new byte[50000];
        int remaining = (int) dataInputStream.readLong();
        while (dataInputStream.available() > 0) {
            dataInputStream.read(buffer, 0, Math.min(buffer.length, remaining));
        }

        return buffer;
    }


    public JSONObject nonFileDataReceiver() throws IOException {
        String dataString = dataInputStream.readUTF();
        JSONObject data = new JSONObject(dataString);

        return data;
    }


    public void sendCommand(JSONObject data) throws IOException {
        dataOutputStream.writeUTF(data.toString());
    }

    public void sendFile(String directory, JSONObject identity) throws IOException {

        dataOutputStream.writeUTF(identity.toString());
        File file = new File(directory);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[50000];
        dataOutputStream.writeLong(file.length());
        while (inputStream.read(buffer) > 0) {
            dataOutputStream.write(buffer);
        }

    }

    public void closeEveryThing(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isConnected() {
        return socket.isConnected();
    }
}
