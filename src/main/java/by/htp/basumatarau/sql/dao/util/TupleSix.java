package by.htp.basumatarau.sql.dao.util;

import by.htp.basumatarau.sql.dao.beans.*;

public class TupleSix <A extends Company,
                    B extends City,
                    C extends Country,
                    D extends Address,
                    E extends Integer,
                    F extends Position> {
    public final A company;
    public final B city;
    public final C country;
    public final D address;
    public final E personnelCount;
    public final F employeePosition;

    public TupleSix(A company,
                    B city,
                    C country,
                    D address,
                    E personnelCount,
                    F employeePosition) {
        this.company = company;
        this.city = city;
        this.country = country;
        this.address = address;
        this.personnelCount = personnelCount;
        this.employeePosition = employeePosition;
    }
}
