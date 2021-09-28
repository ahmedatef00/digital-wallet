package Utils;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.io.Serializable;

@Component
public class TimeProvider implements Serializable {
    private static final long serialVersionID = 646879797938343L;

    public Date now() {
        return new Date();
    }
}
