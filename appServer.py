#!/usr/bin/env python
from flask import Flask, request
from simpledb import Item, SimpleDB, Domain
from dropbox import session, client, rest

sdb = SimpleDB('AKIAIBZWUXL2WLB6ANUA', '5IdVc2KGr2Wc9kX1dO/zL0qDRzR3CE+3uoqZGhPi')
app = Flask(__name__)
APP_KEY = "owry9scatidn35n"
APP_SECRET = "51mowf79e41pkg0"

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
    id = request.args['dropbox_id']
    user = sdb['boxme_user_accounts'][id]
    new_account = {'consumer_key': key, 'consumer_secret': secret, 'dropbox_id' : id}
    if (len(user) == 0):
        user['accounts'] = [new_account]
    else:
        user['accounts'].append(new_account)
    user.save()
    return json.dumps({'success':True}), 200

def get_client(facebook_id, dropbox_id):
    accounts = sdb['boxme_user_accounts'][facebook_id]['accounts']
    if (len(accounts == 0)):
        return None
    sess = session.DropboxSession(APP_KEY, APP_SECRET, 'dropbox')
    for account in accounts:
        if account['dropbox_id'] == dropbox_id:
            sess.set_token(user['consumer_token'], user['consumer_secret'])
            return client.DropboxClient(sess)
    return None

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
            db_id = path_parts[2]
            client = get_client(id, db_id)
            return client.metadata('/'.join(path_paths[3:])['contents']
    return '', 400

@app.route("/putfiles")
def put_files():
    return "Okay, give please?"

if __name__ == "__main__":
    app.run(debug=True)
