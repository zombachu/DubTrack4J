/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Joe Burnard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.sponges.dubtrack4j.internal.subscription;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import io.sponges.dubtrack4j.internal.DubtrackAPIImpl;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class Subscribe {

    private final DubtrackAPIImpl dubtrack;
    private Socket socket;
    private String room;

    public Subscribe(DubtrackAPIImpl dubtrack, String room) throws IOException, URISyntaxException {
        this.dubtrack = dubtrack;
        this.room = room;

        connect();
    }

    public void connect() throws IOException, URISyntaxException {
        Response tokenResponse = dubtrack.getHttpRequester().get("https://api.dubtrack.fm/auth/token");

        if (!tokenResponse.isSuccessful()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                connect();
            } catch (IOException|URISyntaxException e) {
                e.printStackTrace();
            }
            return;
        }

        ResponseBody body = tokenResponse.body();
        String result = body.string();
        body.close();

        JSONObject data = new JSONObject(result).getJSONObject("data");
        String token = data.getString("token");

        IO.Options options = new IO.Options();
        options.hostname = "ws.dubtrack.fm";
        options.secure = true;
        options.path = "/ws";
        options.query = "access_token=" + token;
        options.transports = new String[]{WebSocket.NAME};

        socket = new Socket("wss://ws.dubtrack.fm", options);
        socket.open();

        socket.on("open", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject json = new JSONObject();
                json.put("action", 10);
                json.put("channel", "room:" + room);

                socket.send(json.toString());
            }
        });
        socket.on("close", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    connect();
                } catch (IOException|URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("message", new SubscriptionCallback(dubtrack));
        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println("Error communicating with Dubtrack.");
            }
        });
    }

    public void disconnect() {
        JSONObject json = new JSONObject();
        json.put("action", 12);
        json.put("channel", "room:" + room);

        socket.send(json.toString());
    }
}
