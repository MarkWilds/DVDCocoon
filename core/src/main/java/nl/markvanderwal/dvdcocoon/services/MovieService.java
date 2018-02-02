package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.models.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MovieService extends BaseService<Movie, Integer> {

    @Override
    protected Dao<Movie, Integer> initializeDao(Database database) {
        return database.getMovieRepository();
    }
}
