package SoloProject.SocialMediaApp.service;

//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    //convert app user to a viable authentication object.
//
//    private final AppUserRepository appUserRepository;
//
//    @Autowired
//    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
//        this.appUserRepository = appUserRepository;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) {
//    AppUser appUser = appUserRepository.findByUsername(username);
//    if(appUser == null){
//        throw new UsernameNotFoundException("User not found with username: " + username);
//    }
//    return User.builder()
//            .username(appUser.getUsername())
//            .password(appUser.getPassword())
//            .roles(appUser.getRole())
//            .build();
//    }
//}