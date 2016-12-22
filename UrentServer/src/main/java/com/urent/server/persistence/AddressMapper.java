package com.urent.server.persistence;

import com.urent.server.domain.Address;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/14.
 */
public interface AddressMapper {
    public List<Address> getAddresses(Map map);

    public Long getAddressCount(Map map);

    public Address getAddress(Long id);

    public int addAddress(Address address);

    public void updateAddress(Address address);

    public  void deleteAddress(Address address);

    public int getAssociatedHouseCount(Address address);


}
