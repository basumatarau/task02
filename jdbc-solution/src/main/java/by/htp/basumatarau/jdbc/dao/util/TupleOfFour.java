package by.htp.basumatarau.jdbc.dao.util;

import java.util.Objects;

public class TupleOfFour<A, B, C, D> extends TupleOfTwo<A, B>{
    final public C three;
    final public D four;
    public TupleOfFour(A one, B two, C three, D four) {
        super(one, two);
        this.three = three;
        this.four = four;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TupleOfFour<?, ?, ?, ?> that = (TupleOfFour<?, ?, ?, ?>) o;
        return Objects.equals(three, that.three) &&
                Objects.equals(four, that.four);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), three, four);
    }
}
