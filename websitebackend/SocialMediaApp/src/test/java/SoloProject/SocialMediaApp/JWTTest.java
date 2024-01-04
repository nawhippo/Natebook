//package SoloProject.SocialMediaApp;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class CustomTokenAuthenticationFilterTest {
//
//    private JwtUtil jwtUtil;
//    private UserDetailsService userDetailsService;
//    private CustomTokenAuthenticationFilter filter;
//
//    @BeforeEach
//    public void setUp() {
//        jwtUtil = mock(JwtUtil.class);
//        userDetailsService = mock(UserDetailsService.class);
//        filter = new CustomTokenAuthenticationFilter(jwtUtil, userDetailsService);
//    }
//
//    public void testJwtAuthentication() {
//        JwtUtil mockJwtUtil = mock(JwtUtil.class);
//        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
//
//        String mockUsername = "user";
//        String mockToken = "mock.jwt.token";
//
//        UserDetails mockUserDetails = // Create a mock UserDetails object
//                when(mockUserDetailsService.loadUserByUsername(mockUsername)).thenReturn(mockUserDetails);
//        when(mockJwtUtil.extractUsername(mockToken)).thenReturn(mockUsername);
//        when(mockJwtUtil.validateToken(mockToken, mockUserDetails)).thenReturn(true);
//
//        // Simulate the authentication process
//        if (mockJwtUtil.validateToken(mockToken, mockUserDetails)) {
//            Authentication auth = new UsernamePasswordAuthenticationToken(
//                    mockUserDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        // Check if the user is authenticated
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
//
//        // Assertions
//        assertTrue(isAuthenticated);
//        SecurityContextHolder.clearContext();
//    }
//
//    @AfterEach
//    public void tearDown() {
//        SecurityContextHolder.clearContext(); // Clean up Security Context for other tests
//    }
//}