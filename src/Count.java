import javax.sound.midi.Sequence;
import java.awt.List;
import java.util.Map.Entry;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Count {
    private boolean valid = false;
    private String[] numS = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
    private Map<String, String> map = new HashMap<String, String>();

    Count() {

    }

    String calculate(String str) {
        //initialize map
        for (int i = 0; i < 11; i++) {
            for (int j = i + 1; j < 11; j++) {
                for (int k = j + 1; k < 11; k++) {
                    map.put(numS[i] + numS[j] + numS[k], "");
                }
            }
        }

        int lineNum = 0;
        StringBuilder output = new StringBuilder();
        try {
            Scanner scanner = new Scanner(str);
            while (scanner.hasNextLine()) {
                lineNum++;
                String line = scanner.nextLine();
                int[] n = new int[5];
                String order = line.substring(0, 10);
                n[0] = Integer.parseInt(line.substring(11, 13));
                n[1] = Integer.parseInt(line.substring(14, 16));
                n[2] = Integer.parseInt(line.substring(17, 19));
                n[3] = Integer.parseInt(line.substring(20, 22));
                n[4] = Integer.parseInt(line.substring(23, 25));
                for (int i = 0; i < 5; i++) {
                    for (int j = i + 1; j < 5; j++) {
                        mapAdd(n[i], n[j], order + lineNum);
                    }
                }
            }
            scanner.close();

            output.append("---------统计结果-----------\n");
            output.append("一共有").append(lineNum).append("次开奖结果，其中：\n");
            Map<String, String> sortedMap = map.entrySet().stream()
                    .sorted(Entry.comparingByValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                            (e1,e2) -> e1, LinkedHashMap::new));

            for (String key : sortedMap.keySet()) {
                output.append(key.substring(0, 2)).append(" ");
                output.append(key.substring(2, 4)).append(" ");
                output.append(key.substring(4, 6)).append(": ");
                if (sortedMap.get(key).equals("")) {
                    output.append("最近");
                    output.append(lineNum );
                    output.append("期还未中过奖。\n");
                } else {
                    output.append("已经连续");
                    output.append(Integer.parseInt(sortedMap.get(key).substring(10))-1);
                    output.append("期没中奖，");
                    output.append("最近一次中奖在第").append(sortedMap.get(key).substring(0, 10)).append("期。\n");
                }


            }
            output.append("---------------------------\n");
            valid = true;
        } catch (Exception e) {
            output.delete(0, output.length());
            output.append("(输入数据格式错误！\n提示：先按<清除开奖结果>，然后重新提交。)\n");
//            output.append(e + "\n");
            output.append("--------------------------\n");
        }
        return output.toString();
    }

    boolean inputIsValid() {
        return valid;
    }

    private String getStrSeq(int a, int b, int c) {
        int max, mid, min;
        max = Math.max(a, b);
        max = Math.max(max, c);
        min = Math.min(a, b);
        min = Math.min(min, c);
        if (a != max && a != min) mid = a;
        else if (b != max && b != min) mid = b;
        else mid = c;
        return numS[min - 1] + numS[mid - 1] + numS[max - 1];
    }

    private void mapAdd(int a, int b, String date) {
        for (int i = 1; i < 12; i++) {
            if (i != a && i != b) {
                String key = getStrSeq(a, b, i);
                if (map.get(key).equals("")) map.put(key, date);

            }
        }

    }

}
