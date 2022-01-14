package services;

import entities.User;
import services.dto.UserDetails;
import services.exception.EntityNotFoundException;
import services.exception.UserServiceException;
import services.exception.notValidUserToCreateException;

public interface UserService {

    UserDetails createSignUp(User user) throws UserServiceException, notValidUserToCreateException;

    UserDetails getUserProfileByUsername(String userName) throws UserServiceException, EntityNotFoundException;

    User getUserDbEntityByUserName(String userName) throws UserServiceException, EntityNotFoundException;

    UserDetails updateUserProfile(UserDetails useProfile) throws UserServiceException, EntityNotFoundException;

    User getUserByWalletId(Long walletId) throws UserServiceException, EntityNotFoundException;

}
