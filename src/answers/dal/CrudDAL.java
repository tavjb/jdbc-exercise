package answers.dal;

import java.util.List;

public interface CrudDAL <ID, Entity> {
    ID create(final Entity entity);
    Entity read(final ID id);
    void update(final Entity entity);
    void delete(final ID id);
    List<Entity> readAll();
}