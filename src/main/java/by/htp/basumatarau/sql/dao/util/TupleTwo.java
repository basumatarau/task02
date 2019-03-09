package by.htp.basumatarau.sql.dao.util;

import java.util.Objects;

public class TupleTwo<A extends Integer,
                      B extends Integer> {
    public final A idOne;
    public final B idTwo;

    public TupleTwo(A idOne,
                    B idTwo) {
        this.idOne = idOne;
        this.idTwo = idTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TupleTwo<?, ?> tupleTwo = (TupleTwo<?, ?>) o;
        return Objects.equals(idOne, tupleTwo.idOne) &&
                Objects.equals(idTwo, tupleTwo.idTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOne, idTwo);
    }
}
