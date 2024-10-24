package com.dailycodework.dream_shops.services.user;

import com.dailycodework.dream_shops.dto.UserDto;
import com.dailycodework.dream_shops.exceptions.AlreadyExistsException;
import com.dailycodework.dream_shops.exceptions.RessourceNotFoundException;
import com.dailycodework.dream_shops.models.User;
import com.dailycodework.dream_shops.repositories.UserRepository;
import com.dailycodework.dream_shops.requests.CreateUserRequest;
import com.dailycodework.dream_shops.requests.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow( () -> new RessourceNotFoundException("user not found with the id: "+userId));
    }


    @Override
    public User createUser(CreateUserRequest request){
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExistsException(request.getEmail()+" already exist"));
    }


    @Override
    public User updateUser(UpdateUserRequest request, Long userId){
        return userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow( () -> new RessourceNotFoundException("User not found with the id: "+userId));
    }


    @Override
    public void deleteUser(Long userId) {
        // userRepository::delete est une référence de méthode
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new RessourceNotFoundException("user not found with the id: " + userId);
                }
        );
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }


}
