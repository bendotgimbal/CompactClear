package compact.mobile.config.firebase.database;

import java.io.Serializable;

/**
 * Created by Bendot Gimbal on 08/10/2018.
 */

public class UserKurirFirebase implements Serializable {

    public String Str_IdKurir;
    public String Str_PassKurir;
    public String Str_regId;
    public String Str_date_time;
    private String key;

    // Default constructor required for calls to
    // DataSnapshot.getValue(UserKurirFirebase.class)
    public UserKurirFirebase() {
    }

    public UserKurirFirebase(String id_kurir, String pass_kurir, String regid_kurir, String date_time_now_kurir) {
        this.Str_IdKurir = id_kurir;
        this.Str_PassKurir = pass_kurir;
        this.Str_regId = regid_kurir;
        this.Str_date_time = date_time_now_kurir;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
