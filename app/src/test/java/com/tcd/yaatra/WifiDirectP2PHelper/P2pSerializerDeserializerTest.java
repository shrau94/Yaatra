package com.tcd.yaatra.WifiDirectP2PHelper;

import com.tcd.yaatra.repository.models.Gender;
import com.tcd.yaatra.repository.models.TravellerInfo;
import com.tcd.yaatra.repository.models.TravellerStatus;
import com.tcd.yaatra.utils.EncryptionUtils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class P2pSerializerDeserializerTest {

    private static final String VALUE_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Test
    public void serializeToMap_SerializesTravellersInfoInMapOfStrings() {

        TravellerInfo dummyTraveller = getDummyTravellerInfo();

        Collection<TravellerInfo> travellers = new ArrayList<>();
        travellers.add(dummyTraveller);

        List<Map<String, String>> serializedEncryptedTravellerInfo = P2pSerializerDeserializer.serializeToMap(travellers);

        assertNotNull(serializedEncryptedTravellerInfo);
        assertEquals(1, serializedEncryptedTravellerInfo.size());

        Integer userIdKey = Integer.parseInt(serializedEncryptedTravellerInfo.get(0).keySet().iterator().next());

        String expectedTravellerInfo = getSerializedTravellerInfo(dummyTraveller);

        assertEquals(dummyTraveller.getUserId(), userIdKey);
        assertEquals(expectedTravellerInfo, EncryptionUtils.decrypt(serializedEncryptedTravellerInfo.get(0).get(dummyTraveller.getUserId().toString())));
    }

    @Test
    public void deserializeFromMap_DeserializesTextOfTravellersInfo() {

        TravellerInfo dummyTraveller = getDummyTravellerInfo();

        Collection<TravellerInfo> travellers = new ArrayList<>();
        travellers.add(dummyTraveller);

        List<Map<String, String>> serializedEncryptedTravellerInfo = P2pSerializerDeserializer.serializeToMap(travellers);

        assertNotNull(serializedEncryptedTravellerInfo);
        assertEquals(1, serializedEncryptedTravellerInfo.size());

        HashMap<Integer, TravellerInfo> deserializedTravellerInfoMap = P2pSerializerDeserializer.deserializeFromMap(serializedEncryptedTravellerInfo.get(0));

        assertNotNull(deserializedTravellerInfoMap);
        assertEquals(1, deserializedTravellerInfoMap.size());
        assertTrue(deserializedTravellerInfoMap.containsKey(dummyTraveller.getUserId()));

        TravellerInfo deserializedTravellerInfo = deserializedTravellerInfoMap.get(dummyTraveller.getUserId());

        assertTravellerEquals(dummyTraveller, deserializedTravellerInfo);
    }

    private void assertTravellerEquals(TravellerInfo expectedInfo, TravellerInfo actualInfo) {
        assertEquals(expectedInfo.getUserName(), actualInfo.getUserName());
        assertEquals(expectedInfo.getAge(), actualInfo.getAge());
        assertEquals(expectedInfo.getGender(), actualInfo.getGender());
        assertEquals(expectedInfo.getSourceLatitude(), actualInfo.getSourceLatitude());
        assertEquals(expectedInfo.getSourceLongitude(), actualInfo.getSourceLongitude());
        assertEquals(expectedInfo.getDestinationLatitude(), actualInfo.getDestinationLatitude());
        assertEquals(expectedInfo.getDestinationLongitude(), actualInfo.getDestinationLongitude());
        assertEquals(expectedInfo.getStatus(), actualInfo.getStatus());
        assertEquals(expectedInfo.getRequestStartTime(), actualInfo.getRequestStartTime());
        assertEquals(expectedInfo.getUserRating(), actualInfo.getUserRating());
        assertEquals(expectedInfo.getIpAddress(), actualInfo.getIpAddress());
        assertEquals(expectedInfo.getPortNumber(), actualInfo.getPortNumber());
        assertEquals(expectedInfo.getStatusUpdateTime(), actualInfo.getStatusUpdateTime());
        assertEquals(expectedInfo.getInfoProvider(), actualInfo.getInfoProvider());
    }

    private TravellerInfo getDummyTravellerInfo() {
        String travellerUserName = "Traveller";
        String userName = "TestUser";

        TravellerInfo traveller = new TravellerInfo();
        traveller.setUserId(1);
        traveller.setUserName(travellerUserName);
        traveller.setAge(20);
        traveller.setGender(Gender.MALE);
        traveller.setSourceLatitude(0.0d);
        traveller.setSourceLongitude(0.0d);
        traveller.setDestinationLatitude(0.0d);
        traveller.setDestinationLongitude(0.0d);
        traveller.setStatus(TravellerStatus.SeekingFellowTraveller);
        traveller.setSourceName("test");
        traveller.setDestinationName("test");
        traveller.setModeOfTravel("test");
        traveller.setRequestStartTime(LocalDateTime.now());
        traveller.setUserRating(0.0d);
        traveller.setIpAddress("1.2.3.4");
        traveller.setPortNumber(1234);
        traveller.setStatusUpdateTime(LocalDateTime.now());
        traveller.setInfoProvider(userName);

        return traveller;
    }

    private String getSerializedTravellerInfo(TravellerInfo traveller) {
        return traveller.getUserName() + VALUE_SEPARATOR +
                traveller.getAge() + VALUE_SEPARATOR +
                traveller.getGender() + VALUE_SEPARATOR +
                traveller.getSourceLatitude() + VALUE_SEPARATOR +
                traveller.getSourceLongitude() + VALUE_SEPARATOR +
                traveller.getDestinationLatitude() + VALUE_SEPARATOR +
                traveller.getDestinationLongitude() + VALUE_SEPARATOR +
                traveller.getStatus() + VALUE_SEPARATOR +
                traveller.getSourceName() + VALUE_SEPARATOR +
                traveller.getDestinationName() + VALUE_SEPARATOR +
                traveller.getModeOfTravel() + VALUE_SEPARATOR +
                traveller.getRequestStartTime().format(DATE_TIME_FORMATTER) + VALUE_SEPARATOR +
                traveller.getUserRating() + VALUE_SEPARATOR +
                traveller.getIpAddress() + VALUE_SEPARATOR +
                traveller.getPortNumber() + VALUE_SEPARATOR +
                traveller.getStatusUpdateTime().format(DATE_TIME_FORMATTER) + VALUE_SEPARATOR +
                traveller.getInfoProvider();
    }
}
