package com.lfl.advent2021.days.day16;

import com.lfl.advent2021.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.ImmutableMap;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class Day16 implements LinesConsumer {
    ImmutableMap<Character, String> hexToBinary = Maps.immutable.with('0', "0000")
                                                                .newWithKeyValue('1', "0001")
                                                                .newWithKeyValue('2', "0010")
                                                                .newWithKeyValue('3', "0011")
                                                                .newWithKeyValue('4', "0100")
                                                                .newWithKeyValue('5', "0101")
                                                                .newWithKeyValue('6', "0110")
                                                                .newWithKeyValue('7', "0111")
                                                                .newWithKeyValue('8', "1000")
                                                                .newWithKeyValue('9', "1001")
                                                                .newWithKeyValue('A', "1010")
                                                                .newWithKeyValue('B', "1011")
                                                                .newWithKeyValue('C', "1100")
                                                                .newWithKeyValue('D', "1101")
                                                                .newWithKeyValue('E', "1110")
                                                                .newWithKeyValue('F', "1111");

    @Override
    public void consume(List<String> lines) {
        String binary = lineToBinary(lines.get(0));
        Packet top = Packet.parseBinary(binary);
        int sumVersion = top.sumVersion();
        log.info("Sum version = {}", sumVersion);
        log.info("Evaluate = {}", top.evaluate());
    }

    private String lineToBinary(String line) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < line.length(); index++) {
            sb.append(hexToBinary.get(line.charAt(index)));
        }
        return sb.toString();
    }

    private abstract static class Packet {
        @Getter
        protected int version;
        protected Operators typeId;
        @Getter
        protected int endIndex;

        public Packet(int version, Operators typeId) {
            this.version = version;
            this.typeId = typeId;
        }

        public int sumVersion() {
            return this.version;
        }

        public static Packet parseBinary(String binary) {
            return parseBinary(binary, 0);
        }

        public static Packet parseBinary(String binary, int index) {
            int version = Integer.parseInt(binary.substring(index, index + 3), 2);
            Operators typeId = Operators.of(binary.substring(index + 3, index + 6));
            Packet packet = Operators.LITERAL == typeId ? new LiteralValue(version, typeId) : new Operator(version, typeId);
            packet.parseInnerBinary(binary, index);
            return packet;
        }

        protected abstract void parseInnerBinary(String binary, int index);

        public abstract BigInteger evaluate();
    }

    private static class LiteralValue extends Packet {
        private BigInteger value;

        public LiteralValue(int version, Operators typeId) {
            super(version, typeId);
        }

        protected void parseInnerBinary(String binary, int index) {
            index += 6;//pass version and typeId
            StringBuilder value = new StringBuilder();
            while (binary.charAt(index) == '1') {
                value.append(binary, index + 1, index + 5);
                index += 5;
            }

            value.append(binary, index + 1, index + 5);
            index += 4;
            this.endIndex = index;
            this.value = new BigInteger(value.toString(), 2);
        }

        @Override
        public BigInteger evaluate() {
            return value;
        }
    }

    private static class Operator extends Packet {
        private final List<Packet> subPackets = Lists.mutable.empty();

        public Operator(int version, Operators typeId) {
            super(version, typeId);
        }

        @Override
        public int sumVersion() {
            return this.version + subPackets.stream().mapToInt(Packet::sumVersion).sum();
        }

        protected void parseInnerBinary(String binary, int index) {
            index += 6;//pass version and typeId
            int lengthTypeId = binary.charAt(index) - '0';
            index++;
            if (lengthTypeId == 0) {
                int totalLength = Integer.parseInt(binary.substring(index, index + 15), 2);
                index += 15;
                int targetIndex = index + totalLength;
                while (index < targetIndex) {
                    Packet packet = Packet.parseBinary(binary, index);
                    index = packet.getEndIndex() + 1;
                    subPackets.add(packet);
                }
            } else {
                int numberOfPacket = Integer.parseInt(binary.substring(index, index + 11), 2);
                index += 11;
                for (int packetNumber = 0; packetNumber < numberOfPacket; packetNumber++) {
                    Packet packet = Packet.parseBinary(binary, index);
                    index = packet.getEndIndex() + 1;
                    subPackets.add(packet);
                }
            }
            index--;//because 1 is added
            this.endIndex = index;
        }

        @Override
        public BigInteger evaluate() {
            return typeId.evaluate(subPackets);
        }
    }

    private enum Operators {
        SUM(packets -> packets.stream()
                              .map(Packet::evaluate)
                              .reduce(BigInteger::add)
                              .orElse(BigInteger.ZERO)),
        MULTIPLY(packets -> packets.stream()
                                   .map(Packet::evaluate)
                                   .reduce(BigInteger::multiply)
                                   .orElse(BigInteger.ZERO)),
        MIN(packets -> packets.stream()
                              .map(Packet::evaluate)
                              .min(BigInteger::compareTo)
                              .orElse(BigInteger.ZERO)),
        MAX(packets -> packets.stream()
                              .map(Packet::evaluate)
                              .max(BigInteger::compareTo)
                              .orElse(BigInteger.ZERO)),
        LITERAL(packets -> null),
        GREATER(packets -> packets.get(0).evaluate().compareTo(packets.get(1).evaluate()) > 0 ? BigInteger.ONE : BigInteger.ZERO),
        LESSER(packets -> packets.get(0).evaluate().compareTo(packets.get(1).evaluate()) < 0 ? BigInteger.ONE : BigInteger.ZERO),
        EQUAL(packets -> packets.get(0).evaluate().compareTo(packets.get(1).evaluate()) == 0 ? BigInteger.ONE : BigInteger.ZERO);

        private final Function<List<Packet>, BigInteger> evaluator;

        Operators(Function<List<Packet>, BigInteger> evaluator) {
            this.evaluator = evaluator;
        }

        public BigInteger evaluate(List<Packet> packets) {
            return this.evaluator.apply(packets);
        }

        public static Operators of(String typeId) {
            return Operators.values()[Integer.parseInt(typeId, 2)];
        }
    }
}
