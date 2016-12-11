package io.sponges.dubtrack4j.internal.request;

import io.sponges.dubtrack4j.internal.DubtrackAPIImpl;
import io.sponges.dubtrack4j.util.URL;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.io.IOException;

public class SetRoleRequest implements DubRequest {

    private final DubtrackAPIImpl dubtrack;
    private final String room;
    private final String user;
    private final String role;

    public SetRoleRequest(DubtrackAPIImpl dubtrack, String room, String user, String role) {
        this.dubtrack = dubtrack;
        this.room = room;
        this.user = user;
        this.role = role;
    }

    @Override
    public JSONObject request() throws IOException {
        String url = String.format(URL.EDIT_ROLE.toString(), role, room, user);
        Response response = dubtrack.getHttpRequester().post(url);
        ResponseBody body = response.body();
        String result = body.string();
        body.close();

        return new JSONObject(result);
    }
}
