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

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.rest.Auth;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.ClientOptions;
import io.sponges.dubtrack4j.internal.DubtrackAPIImpl;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.io.IOException;

public class Subscribe {

    private final DubtrackAPIImpl dubtrack;
    private AblyRealtime ably;

    public Subscribe(DubtrackAPIImpl dubtrack, String room) throws AblyException {
        this.dubtrack = dubtrack;

        ClientOptions clientOptions = new ClientOptions();
        clientOptions.environment = "dubtrack";
        clientOptions.fallbackHosts = new String[]{"dubtrack-a.ably-realtime.com"};
        clientOptions.clientId = dubtrack.getAccount().getUuid();
        clientOptions.authCallback = new Auth.TokenCallback() {
            @Override
            public Object getTokenRequest(Auth.TokenParams tokenParams) throws AblyException {
                try {
                    Response response = dubtrack.getHttpRequester().get("https://api.dubtrack.fm/auth/token");
                    ResponseBody body = response.body();
                    String result = body.string();
                    body.close();
                    JSONObject json = new JSONObject(result);
                    JSONObject data = json.getJSONObject("data");

                    Auth.TokenRequest tokenRequest = new Auth.TokenRequest();
                    tokenRequest.keyName = data.getString("keyName");
                    tokenRequest.clientId = data.getString("clientId");
                    tokenRequest.capability = data.getString("capability");
                    tokenRequest.timestamp = data.getLong("timestamp");
                    tokenRequest.nonce = data.getString("nonce");
                    tokenRequest.mac = data.getString("mac");

                    return tokenRequest;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        ably = new AblyRealtime(clientOptions);

        Channel channel = ably.channels.get("room:" + room);
        channel.subscribe(new SubscriptionCallback(dubtrack));

    }

    public AblyRealtime getAbly() {
        return ably;
    }
}
