import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Main {
    public static void main(String[] args) throws Exception {

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;


        // This block configure the logger with handler and formatter
        fh = new FileHandler("log.txt", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.info("----------------------------------");

        Initial initial = new Initial(logger);

        if (initial.isReady()) {
            EventQueue.invokeLater(() -> {
                        ButtonFrame frame = new ButtonFrame();
                        frame.setDefaultCloseOperation((WindowConstants.EXIT_ON_CLOSE));
                        frame.pack();
                        frame.setLocationRelativeTo(null);  // *** this will center your app ***
                        frame.setVisible(true);
                    }
            );
        } else {
            JOptionPane.showMessageDialog(null,
                    "该软件证书信息不正确！\n请检查\"证书.txt\"!",
                    "错误！",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}


class ButtonFrame extends JFrame {
    private JButton buttonCount;
    private JButton buttonCommand;
    private JButton buttonCopy;
    private JPanel panelUp;
    private JPanel panelDown;
    private String defaultText = "您输入3个数字为:\n";
    private String RightNumStr = "";
    private String RightResultStr = "";
    private JTextArea textAreaLeft;
    private JTextArea textAreaRight;

    private Font ft_30;
    private Font ft_25;


    ButtonFrame() {
        int ppi = Toolkit.getDefaultToolkit().getScreenResolution();
        float fSize = ((float) ppi) / 160;
        ft_25 = new Font("黑体", Font.PLAIN, (int) (fSize * 25));
        ft_30 = new Font("黑体", Font.BOLD, (int) (fSize * 30));
        //create listener
        ActionListener insert = new InsertAction();
        ActionListener command = new CommandAction();
        ActionListener clearLeft = new ClearLeft();
        ActionListener clearRight = new ClearRight();
        ActionListener copy = new CopyAction();
        ActionListener count = new CountAction();

        //create panel
        panelUp = new JPanel();
        panelUp.setLayout(new GridLayout(4, 3));

        //row1
        addButton("01", insert);
        addButton("02", insert);
        addButton("03", insert);
        //row2
        addButton("04", insert);
        addButton("05", insert);
        addButton("06", insert);
        //row3
        addButton("07", insert);
        addButton("08", insert);
        addButton("09", insert);
        //row4
        addButton("10", insert);
        addButton("11", insert);
        //add 复制结果
        buttonCopy = new JButton("复制结果");
        Color colorCopy = new Color(30, 144, 255);
        buttonCopy.setEnabled(false);
        buttonCopy.setFont(ft_30);
        buttonCopy.setForeground(colorCopy);
        buttonCopy.addActionListener(copy);
        panelUp.add(buttonCopy);
        //row5
        //提交开奖结果
        buttonCount = new JButton("提交开奖结果");
        Color colorCount = new Color(40, 162, 40);
        buttonCount.setFont(ft_30);
        buttonCount.setForeground(colorCount);
        buttonCount.addActionListener(count);
        panelUp.add(buttonCount);
        //清除开奖结果
        JButton buttonClearLeft = new JButton("清除开奖结果");
        Color colorClearLeft = new Color(242, 38, 19);
        buttonClearLeft.setFont(ft_30);
        buttonClearLeft.setForeground(colorClearLeft);
        buttonClearLeft.addActionListener(clearLeft);
        panelUp.add(buttonClearLeft);
        //提交候选数字
        buttonCommand = new JButton("提交候选数字");
        Color colorCommand = new Color(40, 162, 40);
        buttonCommand.setEnabled(false);
        buttonCommand.setFont(ft_30);
        buttonCommand.setForeground(colorCommand);
        buttonCommand.addActionListener(command);
        panelUp.add(buttonCommand);
        //清除候选数字
        JButton buttonClearRight = new JButton("清除候选数字");
        Color colorClearRight = new Color(242, 38, 19);
        buttonClearRight.setFont(ft_30);
        buttonClearRight.setForeground(colorClearRight);
        buttonClearRight.addActionListener(clearRight);
        panelUp.add(buttonClearRight);


        //add panel to frame
        add(panelUp, BorderLayout.CENTER);


        panelDown = new JPanel();

        textAreaLeft = new JTextArea(20, 50);
        textAreaLeft.setEditable(true);
        textAreaLeft.setFont(ft_25);
        JScrollPane scrollPaneLeft = new JScrollPane(textAreaLeft);
        scrollPaneLeft.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        textAreaRight = new JTextArea(20, 18);
        textAreaRight.setEditable(false);
        textAreaRight.setText(defaultText);
        textAreaRight.setFont(ft_25);
        JScrollPane scrollPaneRight = new JScrollPane(textAreaRight);
        scrollPaneRight.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        panelDown.add(scrollPaneLeft);
        panelDown.add(scrollPaneRight);
        add(panelDown, BorderLayout.SOUTH);


    }

    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setFont(ft_30);
        button.setForeground(new Color(62, 62, 62));
        button.addActionListener(listener);
        panelUp.add(button);
    }

    private class CountAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (textAreaLeft.getText().length() == 0) {
                textAreaLeft.setForeground(Color.RED);
                textAreaLeft.setText("请先输入开奖数据！\n提示：先按<清除开奖结果>，然后重新提交。");
                textAreaLeft.setEditable(false);
                buttonCount.setEnabled(false);
            } else {
                String LeftInputStr = textAreaLeft.getText();
                Count count = new Count();
                String LeftResultStr = count.calculate(LeftInputStr);
                if (count.inputIsValid()) {
                    textAreaLeft.setText(LeftResultStr + LeftInputStr);
                    textAreaLeft.setEditable(false);
                    buttonCount.setEnabled(false);
                } else {
                    textAreaLeft.setEditable(false);
                    textAreaLeft.setText(LeftResultStr + LeftInputStr);
                    textAreaLeft.setForeground(Color.RED);
                }

                textAreaLeft.setCaretPosition(0);
            }


        }

    }

    private class ClearLeft implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            textAreaLeft.setText("");
            textAreaLeft.setForeground(Color.BLACK);
            textAreaLeft.setEditable(true);
            buttonCount.setEnabled(true);

        }

    }

    private class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = " " + event.getActionCommand();
            if (RightNumStr.length() == 0) {
                RightNumStr = RightNumStr + " " + command;
            } else {
                RightNumStr = RightNumStr.replaceAll(command, "");
                RightNumStr = RightNumStr + command;
                if (RightNumStr.length() > 9) {
                    RightNumStr = RightNumStr.substring(RightNumStr.length() - 9, RightNumStr.length());

                }
                if (RightNumStr.length() == 9) {
                    buttonCommand.setEnabled(true);
                }
            }
            textAreaRight.setText(defaultText + RightNumStr);

        }

    }

    private class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            int n1 = Integer.parseInt(RightNumStr.substring(1, 3));
            int n2 = Integer.parseInt(RightNumStr.substring(4, 6));
            int n3 = Integer.parseInt(RightNumStr.substring(7, 9));

            Core core = new Core();
            textAreaRight.setForeground(Color.BLACK);
            RightResultStr = core.calculate(n1, n2, n3);
            textAreaRight.setText(textAreaRight.getText() + "\n-----------\n" + RightResultStr);
            textAreaRight.setCaretPosition(0);
            buttonCommand.setEnabled(false);
            buttonCopy.setEnabled(true);


        }

    }

    private class ClearRight implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            RightNumStr = "";
            textAreaRight.setText(defaultText);
            buttonCommand.setEnabled(false);
            buttonCopy.setEnabled(false);
        }

    }


    private class CopyAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            StringSelection stringSelection = new StringSelection(RightResultStr);
            Clipboard clipbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipbrd.setContents(stringSelection, null);
            textAreaRight.setText(" (结果已复制！)\n" + "------------------\n" + textAreaRight.getText());
            textAreaRight.setCaretPosition(0);


        }

    }


}





