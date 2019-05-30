package search;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;


public class Gui {
	public static JFrame frm;
	public JTextArea txt;
	String seletedItem;
	
	public Gui(LuceneSearch ls) {
		
		frm = new JFrame();
		frm.setTitle("Search");
		frm.setBounds(600, 300, 500, 400);
		
		JPanel nPanel = new JPanel(new GridLayout(3, 2, 20, 10));
		JTextField searchTxt = new JTextField(15);
		JButton button = new JButton("Search by");
		JButton web_button = new JButton("Search online");
		JComboBox<String> dropDown = new JComboBox<String>();
		dropDown.setEditable(false);
		dropDown.setEnabled(true);
		dropDown.addItem("rank");
		dropDown.addItem("song");
		dropDown.addItem("artist");
		dropDown.addItem("year");
		dropDown.addItem("lyric");
		dropDown.addItem("sources");
	
		nPanel.add(new JLabel("Enter keywords:",JLabel.CENTER));  
		nPanel.add(searchTxt);
		nPanel.add(button);
		nPanel.add(dropDown);
		nPanel.add(web_button);
		frm.getContentPane().add(nPanel, BorderLayout.NORTH);
		
		
		JPanel sPanel = new JPanel();
		JEditorPane resultTxt = new JEditorPane();
		//resultTxt.setLineWrap(true);
		resultTxt.setContentType("text/html");
		resultTxt.setEditable(false);
		//resultTxt.setSize(10,10);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(resultTxt);
		scrollPane.setPreferredSize(new Dimension(470, 250));
		sPanel.add(scrollPane);
		//sPanel.add(resultTxt);
		frm.getContentPane().add(sPanel, BorderLayout.SOUTH);
		
		
		
		
		seletedItem = (String)dropDown.getSelectedItem();
		ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					seletedItem = e.getItem().toString();
				}
				
			}
		};
		dropDown.addItemListener(itemListener);
		
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = searchTxt.getText();
				
				String r;
	
				try {
					r = new String(ls.search(s, seletedItem));
					resultTxt.setText(r);
					//resultTxt.append(r);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		resultTxt.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
		        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            try {
		            	resultTxt.setText(null);
		            	//resultTxt.setText(e.getURL().toString());
		            	String s = e.getURL().toString();
		            	s=s.replace("http://","");
		            	int t = Integer.parseInt(s);
		            	String r = new String(ls.searchDetail(t));
		            	
		            	resultTxt.setText(r);      
		            }
		            catch (Exception ex) {
		              ex.printStackTrace();
		            }
		        }
		    }
		});
		
		web_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//String s = searchTxt.getText();
				
				String url = "https://userweb.cs.txstate.edu/~m_t205/websearch.html";
				
				try {
					Desktop.getDesktop().browse(
							new java.net.URI(url));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
}
