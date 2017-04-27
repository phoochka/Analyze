import os
import logging
import json
from sqlalchemy import *
from datetime import datetime
from flask import Flask, request, flash, url_for, redirect, render_template, abort
from flask.ext.sqlalchemy import SQLAlchemy

app = Flask(__name__)
# Keeps Flask from swallowing error messages
app.config['PROPAGATE_EXCEPTIONS'] = True

app.config.from_pyfile('analyzeapp.cfg')
db = SQLAlchemy(app)

class Todo(db.Model):
    __tablename__ = 'todos'
    id = db.Column('todo_id', db.Integer, primary_key=True)
    title = db.Column(db.String(60))
    text  = db.Column(db.String)
    done  = db.Column(db.Boolean)
    pub_date = db.Column(db.DateTime)

    def __init__(self, title, text):
        self.title = title
        self.text = text
        self.done = False
        self.pub_date = datetime.utcnow()

    def __repr__(self):
        return 'Todo: %r' % self.title

class Analyze(db.Model):
    __tablename__ = 'wifi'
    id       = db.Column('wifi_id', db.Integer, primary_key=True)
    mac      = db.Column(db.String)
    ssid     = db.Column(db.String)
    rssi     = db.Column(db.String)
    lspeed   = db.Column(db.Integer)
    netid    = db.Column(db.Integer)
    lat      = db.Column(db.Float)
    lon      = db.Column(db.Float)
    added_date = db.Column(db.DateTime)

    def __init__(self, mac, ssid, rssi, lspeed, netid, lat, lon):
        self.mac  = mac
        self.ssid = ssid
        self.rssi = rssi
        self.lspeed = lspeed
        self.netid = netid
        self.lat = lat
        self.lon = lon
        self.added_date = datetime.utcnow()

    def __repr__(self):
        return 'WiFi Analysis connected to %r' % self.ssid

class Scans(db.Model):
    __tablename__ ='scans'
    id = db.Column('scan_id', db.Integer, primary_key=True)
    mac      = db.Column(db.String)
    ssid     = db.Column(db.String)
    level    = db.Column(db.Integer)
    freq     = db.Column(db.Integer)
    ts       = db.Column(db.String)
    lat      = db.Column(db.Float)
    lon      = db.Column(db.Float)
    added_date = db.Column(db.DateTime)

    def __init__(self, mac, ssid, level, freq, ts, lat, lon):
        self.mac  = mac
        self.ssid = ssid
        self.level = level
        self.freq = freq
        self.ts = ts
        self.lat = lat
        self.lon = lon
        self.added_date = datetime.utcnow()

@app.route("/")
def index():
    return render_template('index.html')

@app.route("/about")
def about():
    return render_template('about.html')

@app.route('/analyze', methods=['POST'])
def wifi():
    if request.method == 'POST':
        app.logger.warning('RECIEVED PHONE REQUEST!!!')
        if not request.json or not 'ssid' in request.json:
            abort(400)
        wifi = Analyze (
                mac   =request.json['mac'],
                ssid  =request.json['ssid'],
                rssi  =request.json['rssi'],
                lspeed=request.json['lspeed'],
                netid =request.json['netid'],
                lat   =request.json['lat'],
                lon   =request.json['lon']
                )
        db.session.add(wifi)
        db.session.commit()
    return 'OK'

@app.route('/scan', methods=['POST'])
def scan():
    if request.method == 'POST':
        app.logger.warning('RECIEVED PHONE REQUEST!!!')
        if not request.json or not 'ssid' in request.json:
            abort(400)
        scan = Scans (
                mac   =request.json['mac'],
                ssid  =request.json['ssid'],
                level =request.json['level'],
                freq  =request.json['freq'],
                ts    =request.json['ts'],
                lat   =request.json['lat'],
                lon   =request.json['lon']
                )
        db.session.add(scan)
        db.session.commit()

@app.route('/testjson', methods=['POST'])
def test():
    if request.method == 'POST':
        app.logger.warning('RECIEVED test REQUEST!!!')
        print('printing json %r' % request.json)

    return 'OK'

@app.route('/wifilist')
def listWifi():
    return render_template('wifiIndex.html',
            wifi = Analyze.query.order_by(Analyze.added_date.desc()).all()
            )

@app.route('/scanlist')
def listScan():
    return render_template('scanIndex.html',
            scans = Scans.query.order_by(Scans.added_date.desc()).limit(150).all()
            )

@app.route('/newtodo', methods=['GET','POST'])
def new():
    if request.method == 'POST':
        todo = Todo(request.form['title'], request.form['text'])
        db.session.add(todo)
        db.session.commit()
        return redirect(url_for('index'))
    return render_template('new.html')

@app.route('/todos')
def todos():
    return render_template('index.html',
            todos=Todo.query.order_by(Todo.pub_date.desc()).all()
            )

    if __name__ == "__main__":
        app.run()
