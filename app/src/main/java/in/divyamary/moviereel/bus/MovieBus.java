package in.divyamary.moviereel.bus;

import com.squareup.otto.Bus;

/**
 * Created by divyamary on 13-01-2016.
 */
public class MovieBus {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
}
