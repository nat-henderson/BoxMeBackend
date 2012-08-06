#!/usr/bin/env python
from flask import Flask, request
from simpledb import Item, SimpleDB, Domain
from dropbox.client import DropboxClient

sdb = SimpleDB('AKIAIBZWUXL2WLB6ANUA', '5IdVc2KGr2Wc9kX1dO/zL0qDRzR3CE+3uoqZGhPi')
app = Flask(__name__)

@app.route("/register")
def register():
    if request.args['consumer_key'] is None:
        return '', 400
    if request.args['consumer_secret'] is None:
        return '', 400
    if request.args['user_id'] is None:
        return '', 400
    key = request.args['consumer_key']
    secret = request.args['consumer_secret']
    id = request.args['user_id']
    user = sdb['boxme_user_accounts'][id]
    new_account = {'consumer_key': key, 'consumer_secret': secret, 'user_id' : user}
    if (len(user) == 0):
        user['accounts'] = [new_account]
    else:
        user['accounts'].append(new_account)
    user.save()
    return json.dumps({'success':True}), 200

@app.route("/listfiles/<path:pathname>")
def list_files(pathname):
    if request.args['user_id'] is None:
        return '', 400
    id = request.args['user_id']
    if not pathname.startswith('/'):
        pathname = '/' + pathname
    path_parts = pathname.split('/')
    if path_parts[0] == 'dropbox':
        if len(path_parts) == 1:
            id_list = sdb[user_id]['accounts']
            return json.dumps(id_list), 200
        else:
            # make client and stuff
            return '', 400
    return '', 400

@app.route("/putfiles")
def put_files():
    return "Okay, give please?"

if __name__ == "__main__":
    app.run(debug=True)
