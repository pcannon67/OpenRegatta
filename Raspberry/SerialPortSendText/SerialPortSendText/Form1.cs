using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO.Ports;
using System.Threading;

namespace SerialPortSendText
{
    public partial class Form1 : Form
    {
        static SerialPort _serialPort;
        static BackgroundWorker _worker;

        public Form1()
        {
            InitializeComponent();

            // Create a new SerialPort object with default settings.
            _serialPort = new SerialPort();

            // Allow the user to set the appropriate properties.
            _serialPort.PortName = "COM5";
            _serialPort.BaudRate = 4800;
            _serialPort.Parity = Parity.None;
            _serialPort.DataBits = 8;
            _serialPort.StopBits = StopBits.One;
            _serialPort.Handshake = Handshake.None;

            // Set the read/write timeouts
            _serialPort.ReadTimeout = 500;
            _serialPort.WriteTimeout = 500;

            _serialPort.Open();

            _worker = new BackgroundWorker();
            _worker.DoWork += new DoWorkEventHandler(_worker_DoWork);
        }

        void _worker_DoWork(object sender, DoWorkEventArgs e)
        {
            if (_serialPort != null
                && _serialPort.IsOpen)
            {
                String[] split = this.textBox1.Text.Split('\n');
                String toSend = "";
                foreach (String str in split)
                {
                    toSend = str+"\n";
                    _serialPort.Write(toSend);
                    this.setLabelText(toSend);
                    if (toSend.StartsWith("$IIVTG"))
                        Thread.Sleep(2500);
                }
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            _worker.RunWorkerAsync();
        }

        private void setLabelText(string p)
        {
            if(this.label1.InvokeRequired){
                this.label1.Invoke((MethodInvoker)delegate {
                    setLabelText(p); 
                });
                return;
            }
            this.label1.Text = p;    
        }
    }

}
