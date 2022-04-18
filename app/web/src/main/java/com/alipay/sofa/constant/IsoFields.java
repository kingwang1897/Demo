package com.alipay.sofa.constant;

/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2007 Enrique Zamudio Lopez

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum IsoFields {
    // |Field title     |no|desc    |type  |len  |fixed  |format|
    F0(0, "MessageType ID", "n", 4, true, null),
    F1(1, "Bitmap", "b", 64, true, null),
    // First BitMap
    F2(2, "Primary Account Number (PAN)", "n", 19, false, "LLVAR"),
    F3(3, "Processing Code", "n", 6, true, null),
    F4(4, "Amount, Transaction", "n", 12, true, null),
    F5(5, "Amount, Settlement", "n", 12, true, null),
    F6(6, "Amount,Cardholder Billing", "n", 12, true, null),
    F7(7, "Transmission Date/Time", "n", 10, true, "MMDDhhmmss"),
    F8(8, "Amount,Cardholder BillingFee", "n", 8, true, null),
    F9(9, "Conversion Rate, Settlement", "n", 8, true, null),
    F10(10, "Conversion Rate, Cardholder Billing", "n", 8, true, null),
    F11(11, "System Trace Audit Number (STAN)", "n", 6, true, null),
    F12(12, "Time, Local Transaction  ", "n", 6, true, "hhmmss"),
    F13(13, "Date, Local Transaction  ", "n", 4, true, "MMDD"),
    F14(14, "Date, Expiration    ", "n", 4, true, "YYMM"),
    F15(15, "Date, Settlement    ", "n", 4, true, "YYMM"),
    F16(16, "Date, Conversion   ", "n", 4, true, "MMDD"),
    F17(17, "Date, Capture", "n", 4, true, null),
    F18(18, "Merchant's Type      ", "n", 4, true, null),
    F19(19, "Merchant Country Code     ", "n", 3, true, null),
    F20(20, "PANExtended", "n", 3, true, null),
    F21(21, "ForwardingInstitution    ", "n", 3, true, null),
    F22(22, "Point Of Service Entry Mode Code  ", "n", 3, true, null),
    F23(23, "Card Sequence Number", "n", 3, true, null),
    F24(24, "NII FunctionCode  ", "n", 3, true, null),
    F25(25, "Point Of Service Condition Code", "n", 2, true, null),
    F26(26, "Point Of Service PIN Capture Code", "n", 2, true, null),
    F27(27, "AuthIdResponseLength     ", "n", 1, true, null),
    F28(28, "Amount,Transaction Fee", "x+n", 8, true, null),
    F29(29, "Amount SettlementFee     ", "x+n", 8, true, null),
    F30(30, "Amount TransactionProcessingFee ", "x+n", 8, true, null),
    F31(31, "Amount SettlementProcessingFee  ", "x+n", 8, true, null),
    F32(32, "Acquiring Institution Identification Code", "n", 11, false, "LLVAR"),
    F33(33, "Forwarding Institution Identification Code", "n", 11, false, "LLVAR"),
    F34(34, "PAN Extended      ", "ns", 28, false, "LLVAR"),
    F35(35, "Track 2 Data", "z", 37, false, "LLVAR"),
    F36(36, "Track 3 Data", "z", 104, false, "LLLVAR"),
    F37(37, "Retrieval Reference Number ", "an", 12, true, null),
    F38(38, "Authorization Identification Response", "an", 6, true, null),
    F39(39, "Response Code", "an", 2, true, null),
    F40(40, "ServiceRestrictionCode   ", "an", 3, true, null),
    F41(41, "Card Acceptor Terminal Identification", "ans", 8, true, null),
    F42(42, "Card Acceptor Identification Code", "ans", 15, true, null),
    F43(43, "Card Acceptor Name/Location", "ans", 40, true, null),
    F44(44, "Additional Response Data", "an", 25, false, "LLVAR"),
    F45(45, "Track 1 Data", "an", 76, false, "LLVAR"),
    F48(48, "Additional Data Private", "an", 999, false, "LLLVAR"),
    F49(49, "Currency Code, Transaction ", "a|n", 3, true, null),
    F50(50, "Currency Code, Settlement", "a|n", 3, true, null),
    F51(51, "Currency Code, Cardholder Billing", "a|n", 3, true, null),
    F52(52, "Pin Data (PIN)", "b", 8, true, null),
    F53(53, "Security Related Control Information", "n", 16, true, null),
    F54(54, "Additional Amounts", "an", 120, false, "LLLVAR"),
    F55(55, "Integrated Circuit Card（ICC）System Related Data ", "ans", 999, false, "LLLVAR"),
    F56(56, "Additional Data", "ansb", 512, false, "LLLVAR"),
    F57(57, "Additional Data Private", "ans", 100, false, "LLLVAR"),
    F59(59, "Detail Inquiring", "ans", 600, false, "LLLVAR"),
    F60(60, "Reserved", "ans", 100, false, "LLLVAR"),
    F61(61, "Cardholder Authentication Information", "ans", 200, false, "LLLVAR"),
    F62(62, "Switching Data", "ans", 200, false, "LLLVAR"),
    F63(63, "Financial Network Data", "ansb", 512, false, "LLLVAR"),
    // Secondary BitMap,
    F70(70, "Network Management Information Code", "n", 3, true, null),
    F90(90, "Original Data Elements", "an", 42, false, null),
    F96(96, "Message Security Code", "b", 64, false, null),
    F100(100, "Receiving Institution Identification Code", "an", 11, false, "LLVAR"),
    F102(102, "Account Identification 1", "an", 28, false, "LLVAR"),
    F103(103, "Account Identification 2", "an", 28, false, "LLVAR"),
    F104(104, "Additional Data", "ansb", 512, false, "LLLVAR"),
    F113(113, "Additional Data", "ansb", 512, false, "LLLVAR"),
    F116(116, "Additional Data", "ansb", 512, false, "LLLVAR"),
    F117(117, "Additional Data", "ansb", 256, false, "LLLVAR"),
    F121(121, "CUPS Reserved", "ans", 100, false, "LLLVAR"),
    F122(122, "Acquiring Institution Reserved", "ans", 100, false, "LLLVAR"),
    F123(123, "Issuer Institution Reserved", "ans", 100, false, "LLLVAR"),
    F125(125, "Additional Data", "ansb", 256, false, "LLLVAR"),
    F126(126, "Additional Data", "ansb", 256, false, "LLLVAR"),
    F128(128, "Message Authentication Code (MAC) ", "b", 64, true, null);


    private final int no;
    private final String desc;
    private final String type;
    private final int length;
    private final boolean fixed;
    private final String format;

    IsoFields(int no, String desc, String type, int length, boolean fixed, String format) {
        this.no = no;
        this.desc = desc;
        this.type = type;
        this.length = length;
        this.fixed = fixed;
        this.format = format;
    }

    private static Map<Integer, IsoFields> map = new HashMap<>();
    private static JSONObject json = new JSONObject(true);

    static {
        for (IsoFields field : IsoFields.values()) {
            map.put(field.getNo(), field);
            json.put(String.valueOf(field.no), field.toJson());
        }
    }

    public int getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public boolean isFixed() {
        return fixed;
    }

    public String getFormat() {
        return format;
    }

    public static IsoFields of(int no) {
        return map.get(no);
    }

    private JSONObject toJson() {
        JSONObject json = new JSONObject(true);
        json.put("no", no);
        json.put("desc", desc);
        json.put("type", type);
        json.put("length", length);
        json.put("fixed", fixed);
        json.put("format", format);
        return json;
    }

    /**
     * Json key by no, Used by front.
     *
     * @return
     */
    public static JSONObject json() {
        return json;
    }
}
