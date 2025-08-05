package mad.apps.sabenza.di;

import android.content.Context;

public class DaggerService {

    public static final String SERVICE_NAME = DaggerService.class.getName();

    public static SabenzaComponent getDaggerComponent(Context context) {
        return (SabenzaComponent) context.getApplicationContext().getSystemService(SERVICE_NAME);
    }
}
