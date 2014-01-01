package ds;

import com.mebigfatguy.ds.DSLocalizer;

public class TestLocalizer implements DSLocalizer {

    @Override
    public String getLocalString(String key) {
        return key;
    }

}
