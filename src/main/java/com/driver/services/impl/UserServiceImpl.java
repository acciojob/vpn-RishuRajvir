package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user.
        // Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer

        //convert to uppercase to ignore uppercase or lower case
        String countryName1 = countryName.toUpperCase();
        //Throw exception
        if (!countryName1.equals("IND") && !countryName1.equals("USA") && !countryName1.equals("CHI") && !countryName1.equals("JPN")) throw new Exception("Country not found");
        Country country = new Country(CountryName.valueOf(countryName1.toString()),CountryName.valueOf(countryName1).toCode());


        //Create user and set values
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);


        //add user to country
        country.setUser(user);
        user.setOriginalCountry(country);
        //save user to get user obejct and by that userId
        user = userRepository3.save(user);
        //save the originalIp in given format
        user.setOriginalIp(new String(user.getOriginalCountry().getCode() + "." + user.getId()));
        //save user
        user = userRepository3.save(user);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        //subscribe to the serviceProvider by adding it to the list of providers and return updated User
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        User user = userRepository3.findById(userId).get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        serviceProviderRepository3.save(serviceProvider);
        return user;

    }
}
