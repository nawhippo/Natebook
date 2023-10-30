package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            AppUser appUser = accountService.getAccountDetails(username);
            if (appUser == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return appUser;
        }
}
