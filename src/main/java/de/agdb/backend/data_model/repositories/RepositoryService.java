package de.agdb.backend.data_model.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

    @Autowired
    private UsersRepository usersRepository;

    public DateWrapperRepository getDateWrapperRepository() {
        return dateWrapperRepository;
    }

    @Autowired DateWrapperRepository dateWrapperRepository;

    public TimeLocationWrapperRepository getTimeLocationWrapperRepository() {
        return timeLocationWrapperRepository;
    }

    public CategoriesWrapperRepository getCategoriesWrapperRepository() {
        return categoriesWrapperRepository;
    }

    @Autowired TimeLocationWrapperRepository timeLocationWrapperRepository;
    @Autowired CategoriesWrapperRepository categoriesWrapperRepository;

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }
}
