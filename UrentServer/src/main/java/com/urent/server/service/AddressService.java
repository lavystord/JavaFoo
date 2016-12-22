package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Address;
import com.urent.server.domain.Area;
import com.urent.server.persistence.AddressMapper;
import com.urent.server.persistence.AreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/14.
 */
@Service
public class AddressService {
    @Autowired
    AddressMapper addressMapper;

    @Autowired
    AreaMapper areaMapper;


    public Address addAddress(Address address) {

        addressMapper.addAddress(address);
        return address;
    }


    public List<Address> getAddresses(Map<String, Object> queryFilters){
        return addressMapper.getAddresses(queryFilters);
    }

    public Long getAddressCount(Map<String, Object> queryFilters) {
        return addressMapper.getAddressCount(queryFilters);
    }

    public Address getAddress(Long id) {
        Address address;
         address= addressMapper.getAddress(id);
        address.setArea(areaMapper.getAreaWithAscendants(address.getArea().getId()));

    return  address;

    }

    public Address updateAddress(Address address) {
        addressMapper.updateAddress(address);
        return address;
    }

    public void  deleteAddress(Address address) {
        if (addressMapper.getAssociatedHouseCount(address)==0) {
            addressMapper.deleteAddress(address);
        }
        else {
            throw new USException(USException.ErrorCode.DeleteReferentResource,"存在关联此地址的房屋，无法删除");
        }
    }


    /**
     * 根据条件定位返回小区，这里期望根据longitude和latitude来返回最近的地点，但暂时支持不了
     * 所以先就和getAddress一样返回
     * @param queryFilters
     * @return
     */
    public List<Address> locateAddresses(Map<String, Object> queryFilters) {
        return addressMapper.getAddresses(queryFilters);
    }

    public Long locateAddressCount(Map<String, Object> queryFilters) {
        return addressMapper.getAddressCount(queryFilters);
    }
}
