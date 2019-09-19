package acme;

import com.acme.common.Receiver;
import com.acme.protocol.FixedLengthProtocol;
import com.acme.protocol.FrameInfo;
import com.acme.protocol.Protocol;
import com.acme.server.AIOServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author acme
 * @date 2019/9/3 8:07 PM
 */
public class AIOServerTest {
    static int i = 0;
    public static void main(String[] args) throws InterruptedException {
        AIOServer aioServer = new AIOServer(new FixedLengthProtocol());
        aioServer.setReceiveHeartBeat(true);
        aioServer.start();
        Map<String, List<FrameInfo>> map = new ConcurrentHashMap<>();
        aioServer.receive(new Receiver() {
            @Override
            public void recv(FrameInfo frameInfo, String key) {
                if((i % 100 == 0) && frameInfo.getFrameHead().getType() == Protocol.HEART_BEAT_TYPE){
                    System.out.println("-------heart_beat-------");
                    System.out.println(frameInfo);
                    System.out.println("-------heart_beat-------");
                } else if(frameInfo.getFrameHead().getType() == Protocol.CONTENT_TYPE){
                    System.out.println(++i);
//                    System.out.println(frameInfo);
                    if(!map.containsKey(frameInfo.getKey())){
                        map.put(frameInfo.getKey(), new ArrayList<>());
                    }
                    map.get(frameInfo.getKey()).add(frameInfo);
                }
            }
        }, 10);

        while (true){
//            map.forEach((v, k) -> {
//                System.out.println("key:"+v + ", size:" + k.size());
//                System.out.println();
//                //aioServer.send(v, GenString.getBytes(1));
//            });
            Thread.sleep(1000);
        }
    }
}
