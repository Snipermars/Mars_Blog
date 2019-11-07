package com.liupeidong.multiLearning.usingTransactionalMemory.stm;

import akka.actor.*;

import java.util.concurrent.TimeUnit;

/**
 * AkkaExample class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/26 0026 16:58
 */
public class AkkaExample {

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("actor-demo-java");
        ActorRef hello = system.actorOf(Props.create(Hello.class));
        hello.tell("Bob", ActorRef.noSender());
        try{
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e){
            system.shutdown();
        }
    }

    private static class Hello extends UntypedActor{
        public void onReceive(Object message) throws Exception{
            if(message instanceof String){
                System.out.println("Hello " + message);
            }
        }
    }


}
