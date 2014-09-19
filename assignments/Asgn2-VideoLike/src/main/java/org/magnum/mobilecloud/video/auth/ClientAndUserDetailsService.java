/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mobilecloud.video.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

/**
 * A class that combines a UserDetailsService and ClientDetailsService
 * into a single object.
 * 
 * @author jules
 *
 */
public class ClientAndUserDetailsService implements UserDetailsService,
		ClientDetailsService {

	private final ClientDetailsService clients;
	private final UserDetailsService users;
	
//	@Autowired
//	private final UserRepository userRepo = new UserRepository(); 
	
	private final ClientDetailsUserDetailsService clientDetailsWrapper;

	public ClientAndUserDetailsService(ClientDetailsService clients, UserDetailsService users) {
		super();
		this.users = users;
		this.clients = clients;
		clientDetailsWrapper = new ClientDetailsUserDetailsService(clients);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId)
			throws ClientRegistrationException {
		return clients.loadClientByClientId(clientId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = null;
		try{
			user = users.loadUserByUsername(username);
		}catch(UsernameNotFoundException e){
			user = clientDetailsWrapper.loadUserByUsername(username);
		}
		return user;
	}

}
