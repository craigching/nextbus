package net.webasap.nextbus;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import net.webasap.nextbus.domain.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // TODO Command line argument parsing
        // TODO Usage

//        val route = args[0];
//        val stop = args[1];
//        val direction = args[2];
//
//        new TimeToNextBusService().getTimeToNextBus(route, stop, direction);
        val service = new TimeToNextBusService();

        service.getTimeToNextBus("METRO Blue Line", "Target Field Station Platform 1", "south");

    }
}
