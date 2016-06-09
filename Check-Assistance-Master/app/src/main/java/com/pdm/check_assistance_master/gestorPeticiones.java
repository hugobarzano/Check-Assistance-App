package com.pdm.check_assistance_master;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by hugo on 12/04/16.
 */
public class gestorPeticiones {


    private static RequestQueue cola;

    public static RequestQueue getCola() {
        return cola;
    }

    public static void setCola(Context context) {
        gestorPeticiones.cola = Volley.newRequestQueue(context);
    }
}
