package services;

import entities.User;
import entities.dto.UserDetails;
import services.exception.UserNotFoundException;
import services.exception.UserServiceException;
import services.exception.notValidUserToCreateException;

public interface UserService {

    UserDetails createSignUp(User user) throws UserServiceException, notValidUserToCreateException;

    UserDetails getUserProfileByUsername(String userName) throws UserServiceException, UserNotFoundException;

    User getUserDbEntityByUserName(String userName) throws UserServiceException, UserNotFoundException;

    UserDetails updateUserProfile(UserDetails useProfile) throws UserServiceException, UserNotFoundException;

    User getUserByWalletId(Integer walletId) throws UserServiceException, UserNotFoundException;

}
