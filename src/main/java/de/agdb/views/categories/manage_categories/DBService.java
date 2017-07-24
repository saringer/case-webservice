package de.agdb.views.categories.manage_categories;

import de.agdb.backend.entities.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Riva on 26.03.2017.
 */
@Service
public class DBService {

    @Autowired
    private JdbcTemplate jdbcTemp;




    public List<Categories> getCategories() {

        String sql = "SELECT * FROM categories";
        List<Categories> list = jdbcTemp.queryForList(sql, Categories.class);
        return list;
    }


}
