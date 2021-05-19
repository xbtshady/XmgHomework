package com.example.oauth.hash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

class Node {
    private String ip;
    private String name;

    public Node(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ip;
    }
}

public class ConsistentHash {
    // 每个机器节点关联的虚拟节点数量
    private final int number;
    // 环形虚拟节点
    private final SortedMap<Long, Node> circle = new TreeMap<Long, Node>();

    public ConsistentHash(int number, List<Node> nodes) {
        this.number = number;
        for (Node node : nodes) {
            add(node);
        }
    }

    /**
     * 增加一个Node
     *
     * @param node
     */
    public void add(Node node) {
        for (int i = 0; i < number; i++) {
            circle.put(hash(node.toString() + i), node);
        }
    }

    /**
     * 减一个Node
     *
     * @param node
     */
    public void remove(Node node) {
        for (int i = 0; i < number; i++) {
            circle.remove(hash(node.toString() + i));
        }
    }

    /**
     * 获取一个Node
     *
     * @param key
     * @return
     */
    public Node get(String key) {
        long hash = hash(key);
        // 沿环的顺时针找到一个虚拟节点
        if (!circle.containsKey(hash)) {
            SortedMap<Long, Node> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    /**
     * MurMurHash算法
     *
     * @param key
     * @return
     */
    private static Long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;

    }


    public static void main(String[] args) {
        //每个节点上保存的记录条数
        Map<String, Integer> map = new HashMap();
        //10个节点
        List<Node> nodes = new ArrayList();
        for (int i = 1; i <= 10; i++) {
            map.put("IP_" + i, 0); // 初始化记录
            Node node = new Node("IP_" + i, "Node" + i);
            nodes.add(node);
        }

        //每个节点引入1024个虚拟节点
        ConsistentHash consistentHash = new ConsistentHash(1024, nodes);
        //模拟查找节点
        for (int i = 0; i < 10000; i++) {
            String data = UUID.randomUUID().toString() + i;
            Node node = consistentHash.get(data);
            map.put(node.getIp(), map.get(node.getIp()) + 1);
        }
        for (int i = 1; i <= 10; i++) {
            System.out.println("IP_" + i + "节点记录条数：" + map.get("IP_" + i));
        }
    }
}

