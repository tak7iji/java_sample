package org.sample.client;

import java.util.concurrent.CountDownLatch;

public class WSTest {
    CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-") && args.length >= (i + 1)) {
                try {
                    Options.valueOf(args[i].substring(1)).setValue(args[++i]);
                } catch (IllegalArgumentException iae) {
                    // ignore
                }
            }
        }

        int num = Integer.parseInt(Options.n.getValue());
        int size = Integer.parseInt(Options.size.getValue());
        long min = 0;
        long max = 0;
        long sum = 0;
        long res = 0;

        for(int cnt = 1; cnt <= 10; cnt++) {
            StringBuilder builder = new StringBuilder();
            builder.append(cnt*size);

            for(int i = 0; i < num; i++) {
                res = new WSTest().startTest(Options.host.getValue(), Integer.toString(cnt*size));
                max = Math.max(max, res);
                min = (min != 0) ? Math.min(min, res) : max;
                sum += res;
                builder.append(",").append(res);
                Thread.sleep(5000);
            }
            builder.append(",").append(((sum - max - min)/8.0));
            System.out.println(builder.toString());
            min = max = sum = 0;
        }
    }

    public long startTest(String host, String size) throws Exception {
        IOSocketClient client = new IOSocketClient(this);
        client.connect(host);
        client.getIOSocket().send(size);
        long send = System.currentTimeMillis();
        latch.await();
        client.close();
        return client.getTime() - send;
    }

    enum Options {
        host("localhost"), n("1"), size("1000000");

        private String value;

        Options(String defaultValue) {
            this.value = defaultValue;
        }

        String getValue() {
            return this.value;
        }

        void setValue(String value) {
            this.value = value;
        }
    }
    
    public void countDown() {
        latch.countDown();
    }
}
