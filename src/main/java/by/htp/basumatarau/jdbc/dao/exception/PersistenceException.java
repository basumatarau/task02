package by.htp.basumatarau.jdbc.dao.exception;

public class PersistenceException extends Exception {

    public PersistenceException(){
        super();
    }

    public PersistenceException(String msg){
        super(msg);
    }

    public PersistenceException(String msg, Exception e){
        super(msg, e);
    }

    public PersistenceException(Exception e){
        super(e);
    }
}
