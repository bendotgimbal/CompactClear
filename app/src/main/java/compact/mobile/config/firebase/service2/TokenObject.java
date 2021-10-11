package compact.mobile.config.firebase.service2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yosua Tony Batara on 21/05/2018.
 */

public class TokenObject {
    @SerializedName("token")
    private String token;
    public TokenObject(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
