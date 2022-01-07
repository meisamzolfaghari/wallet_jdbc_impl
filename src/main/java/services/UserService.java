package services;

import entities.User;
import entities.dto.UserDetails;
import services.exception.UserServiceException;

public interface UserService {

    UserDetails createSignUp(User user) throws UserServiceException;

    UserDetails getUserProfileByUsername(String userName) throws UserServiceException;

    User getUserDbEntityByUserName(String userName) throws UserServiceException;

    UserDetails updateUserProfile(UserDetails useProfile) throws UserServiceException;

}
