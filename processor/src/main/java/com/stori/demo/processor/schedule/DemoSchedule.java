package com.stori.demo.processor.schedule;

import com.stori.demo.processor.core.service.MessageProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoSchedule {

    @Autowired
    private MessageProcessService messageProcessService;

//    @Scheduled(fixedDelay = 1000L)
//    public void demo() {
//        System.out.println("Start!!!");
//
////        messageMqListenerNew.messageHandle("2e1230363538343830313030303030303030303031303030303030300000000030303030303030300030303030303038303082200000000108000400000110000001303431333134303030303438303735323531324e4b323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443231313630303030303030303030303030303130313030303030303030303839323031303030304142434445464748");
////        messageMqListenerNew.messageHandle("2e12303635383438303130303030303030303030313030303030303000000000303030303030303000303030303030313030F23E60818EC0800000000000100000003136343736313733393030313031303131393030303030303030303030303031323330303033303430353431333330303132303530303132303530323035323730353032303535333939303836303830353130303031303531303030323230363330353030303031343132333332333030323931313030303131303031303130303130313030313038343030383632323830303030");
//
//        String message = "2e12303333353438303130303030303030303030313030303030303000000000303030303030303000303030303030313030F23E608188C08080000000001000000131363437363137333930303130313031313930303030303030303030303030313233303030333034303534313333303031323035303031323035303230353237303530323035353339393038363038303730353130303031303730353130303032323036333035303030303134323931313030303131363030303030303030303030303038343041423132333435362020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020303836323238303030304142434445464748";
//
//        StoriMessage request = new StoriMessage();
//        request.setSocketId("111000");
//        request.setMessageChannel("123456");
//        request.setOriginMessage(message);
//        messageProcessService.createResponse(request);
//        System.out.println("End!!!");
//    }
}
