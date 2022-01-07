package entities;

public abstract class BaseEntity<I> {

    public static final String ID_SQL = "id";
    protected I id;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public boolean isPersisted() {
        return id != null;
    }

    public abstract String getTableName();
}
