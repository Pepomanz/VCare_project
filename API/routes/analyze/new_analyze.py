import pandas as pd
import numpy as np
import arrow
import dateutil
import matplotlib.pyplot as plt
import math
import pymysql
from sklearn.cluster import KMeans
import pprint
import math
import datetime

columns=['id', 'dateTime', 'carId', 'sensorId', 'sensorName', 'value']
df_sensor = pd.read_csv("./Sensor_data3.csv", skiprows=1, names = ['dateTime','carId','sensorId','sensorName','value'])
df_sensor = df_sensor.iloc[230000:]
sensor_dict = df_sensor[['sensorId','sensorName']][0:36]
reshape_sensor = {}
idx = 0
tz = 'Asia/Bangkok'
print("************************** START creating new dict shape")
while idx < df_sensor.shape[0]-1:
    buffer_sensor = {}
    time = df_sensor.iloc[idx].dateTime
    while time == df_sensor.iloc[idx].dateTime and idx < df_sensor.shape[0]-1: #check if time changes
        if idx % 1000 == 0 or idx == df_sensor.shape[0]-1:
            print("-----Reshaping index",idx,"of",df_sensor.shape[0]-1)
        buffer_sensor[df_sensor.iloc[idx].sensorId] = df_sensor.iloc[idx].value
        idx += 1
    if idx==df_sensor.shape[0]-1:
        buffer_sensor[df_sensor.iloc[df_sensor.shape[0]-1].sensorId] = df_sensor.iloc[df_sensor.shape[0]-1].value #solving the last index problem
    reshape_sensor[arrow.get(time).replace(tzinfo=dateutil.tz.gettz(tz))] = buffer_sensor

print("************************** START creating sensorId list")
#Create Sensor ID list to use as ref
sensorId_list = []
for idx in range(int(df_sensor.shape[0])):
    if df_sensor.iloc[idx].sensorId not in sensorId_list:
        sensorId_list.append(df_sensor.iloc[idx].sensorId)

print("************************** START finaling dict")   
sensor_value_dict = {}
new_sensor_value_dict = {}
for sensorId in sensorId_list:
    sensor_value_dict[sensorId] = []
    new_sensor_value_dict[sensorId] = []
    
index_time = list(reshape_sensor.keys())
for date in index_time:
    for sensorId in sensorId_list:
        if sensorId in reshape_sensor[date].keys():
            sensor_value_dict[sensorId].append(reshape_sensor[date][sensorId])
        else:
            sensor_value_dict[sensorId].append("None")


sensor_value_dict['dateTime'] = index_time
new_sensor_value_dict['dateTime'] = []

print("************************** START Adding missing time")  
for idx in range(len(sensor_value_dict['dateTime'])):
    
    if idx != 0 and sensor_value_dict['dateTime'][idx] - sensor_value_dict['dateTime'][idx-1] > datetime.timedelta(0, 10):
        while sensor_value_dict['dateTime'][idx] - new_sensor_value_dict['dateTime'][-1] > datetime.timedelta(0, 10):
            print(new_sensor_value_dict['dateTime'][-1])
    #        print(sensor_value_dict['dateTime'][idx-1], sensor_value_dict['dateTime'][idx])
            for key in sensor_value_dict.keys():
                if key != 'dateTime':
                    new_sensor_value_dict[key].append(0)
                else:
                    new_sensor_value_dict[key].append(new_sensor_value_dict['dateTime'][-1].replace(seconds=+5))

    elif sensor_value_dict['dateTime'][idx] - sensor_value_dict['dateTime'][idx-1] <= datetime.timedelta(0, 10):
        for key in sensor_value_dict.keys():
            new_sensor_value_dict[key].append(sensor_value_dict[key][idx])
    else:
        for key in sensor_value_dict.keys():
            new_sensor_value_dict[key].append(sensor_value_dict[key][0])  
        
        
        
sensor_value_dict = new_sensor_value_dict
df_sensor_value = pd.DataFrame(sensor_value_dict).set_index('dateTime')

start_date = "2018-05-05 14:00"
end_date = "2018-05-05 23:59"
print("************************** Filtering Time")
start_date = arrow.get(start_date).replace(tzinfo=dateutil.tz.gettz(tz))
end_date = arrow.get(end_date).replace(tzinfo=dateutil.tz.gettz(tz))
df_sensor_value = df_sensor_value.drop(df_sensor_value[df_sensor_value.index < start_date].index)
df_sensor_value = df_sensor_value.drop(df_sensor_value[df_sensor_value.index > end_date].index)


print("************************** Filtering Column")
usable_sensorId = [4, 5, 12, 13, 47, 49, 66]
df_sensor_value = df_sensor_value.loc[:,usable_sensorId]

df_position = pd.read_csv('GPS_data3.csv',skiprows=1, names = ['dateTime','carId','DTC','latitude','longtitude','distance'])

print("************************** Changing time in df_position to arrow and filter_time")
for idx in range(df_position.shape[0]):
    print("--- Converting df_position.dateTime to arrow",idx)
    df_position.set_value(idx, 'dateTime', arrow.get(df_position.iloc[idx]['dateTime']).replace(tzinfo=dateutil.tz.gettz(tz)))

df_position = df_position.set_index('dateTime')
df_position = df_position.drop(df_position[df_position.index < start_date].index)
df_position = df_position.drop(df_position[df_position.index > end_date].index)

print("************************** Deleting duplicate datetime")  
delete_idx = []  
for idx in range(df_position.shape[0]):
    if idx != 0 and df_position.index[idx] == df_position.index[idx-1]:
        print("-"*50,idx,"of",df_position.shape[0])
        print(df_position.iloc[idx])      
        delete_idx.append(idx)
df_position = df_position.drop(df_position.iloc[delete_idx].index)  

  

print("************************** Merging DataFrames")    
for idx in range(df_position.shape[0]):
    if df_sensor_value.index[idx] in df_position.index:
        print(df_sensor_value.index[idx],df_position.index[idx],df_sensor_value.index[idx] in df_position.index)
        print(type(df_position.loc[df_sensor_value.index[idx]]))
        print("-"*50)
        df_sensor_value.set_value(df_sensor_value.index[idx], 'latitude', df_position.loc[df_sensor_value.index[idx]]['latitude'])
        df_sensor_value.set_value(df_sensor_value.index[idx], 'longtitude', df_position.loc[df_sensor_value.index[idx]]['longtitude'])
        df_sensor_value.set_value(df_sensor_value.index[idx], 'distance', df_position.loc[df_sensor_value.index[idx]]['distance'])


df_car = df_sensor_value.copy() 
usable_sensorName = ['load', 'temp', 'rpm', 'speed', 'fuel', 'mileage', 'voltage']

columns = {}
for idx in range(len(usable_sensorId)):
    columns[usable_sensorId[idx]] = usable_sensorName[idx]
df_car = df_car.rename(columns=columns)

for col in df_car.keys():
    for idx in range(df_car.shape[0]):
        if df_car.loc[:,col][idx] != 'None':
            df_car.set_value(df_car.index[idx], col, float(df_car.loc[:,col][idx]))
        else:
            df_car.set_value(df_car.index[idx], col, -1.0)


del df_sensor
del df_position
del df_sensor_value


print("************************** cleaning data") 
def clean_none_value(df_car, col):
    #fill missing value
    for idx in range(df_car.shape[0]):
        if col not in ['latitude', 'longtitude', 'distance']:
            if df_car.loc[:,col][idx] != 'None':
                df_car.set_value(df_car.index[idx], col, float(df_car.loc[:,col][idx]))
            elif idx != 0 and idx != (df_car.shape[0])-1  and df_car.loc[:,col][idx] == 'None':
                i = idx
                while df_car.loc[:,col][i] == 'None' or df_car.loc[:,col][i] < 0:
                    i -= 1
                    if df_car.loc[:,col][i] != 'None' or df_car.loc[:,col][i] >= 0:
                        before_val = df_car.loc[:,col][i]
                j = idx
                while df_car.loc[:,col][j] == 'None' or df_car.loc[:,col][i] < 0:
                    j += 1
                    if df_car.loc[:,col][j] != 'None' or df_car.loc[:,col][i] >= 0:
                        after_val = df_car.loc[:,col][j]
                df_car.set_value(df_car.index[idx], col, (float(before_val) + float(after_val))/2)
            elif idx == 0:
                j = idx
                while df_car.loc[:,col][j] == 'None' or df_car.loc[:,col][i] < 0:
                    j += 1
                    if df_car.loc[:,col][j] != 'None' or df_car.loc[:,col][i] >= 0:
                        after_val = df_car.loc[:,col][j]
                df_car.set_value(df_car.index[idx], col, float(after_val))
            elif idx == (df_car.shape[0])-1:
                i = idx
                while df_car.loc[:,col][i] == 'None' or df_car.loc[:,col][i] < 0:
                    i -= 1
                    if df_car.loc[:,col][i] != 'None' or df_car.loc[:,col][i] >= 0:
                        before_val = df_car.loc[:,col][i]
                df_car.set_value(df_car.index[idx], col, float(before_val))
            print("Missing", col, df_car.index[idx])
        else:
            if math.isnan(df_car.loc[:,col][idx]) == False:
                df_car.set_value(df_car.index[idx], col, float(df_car.loc[:,col][idx]))
            elif idx != 0 and idx != (df_car.shape[0])-1  and df_car.loc[:,col][idx] == 'None':
                i = idx
                while math.isnan(df_car.loc[:,col][i]) == True:
                    i -= 1
                    if math.isnan(df_car.loc[:,col][i]) == False:
                        before_val = df_car.loc[:,col][i]
                j = idx
                while math.isnan(df_car.loc[:,col][j]) == True:
                    j += 1
                    if math.isnan(df_car.loc[:,col][j]) == False:
                        after_val = df_car.loc[:,col][j]
                df_car.set_value(df_car.index[idx], col, (float(before_val) + float(after_val))/2)
            elif idx == 0:
                j = idx
                while math.isnan(df_car.loc[:,col][j]) == True:
                    j += 1
                    if math.isnan(df_car.loc[:,col][j]) == False:
                        after_val = df_car.loc[:,col][j]
                df_car.set_value(df_car.index[idx], col, float(after_val))
            elif idx == (df_car.shape[0])-1:
                i = idx
                while math.isnan(df_car.loc[:,col][i]) == True:
                    i -= 1
                    if math.isnan(df_car.loc[:,col][i]) == False:
                        before_val = df_car.loc[:,col][i]
                df_car.set_value(df_car.index[idx], col, float(before_val))
        
    return df_car
    
    
for col in df_car.keys():
    df_car = clean_none_value(df_car, col)
   
print("************************** cleaning minus data") 
def clean_minus_value(df_car, col):
    for idx in range(df_car.shape[0]):
        if df_car.loc[:,col][idx] < 0:
            df_car.set_value(df_car.index[idx], col, 0)
    return df_car

for col in df_car.keys():
    df_car = clean_minus_value(df_car, col)               
    
def clean_data_by_IQR(df_car, col):
    #clean outliers
#    print(col)
#    print(df_car[col])
    Q1 = df_car[col].quantile([.25]).values[0]
    Q3 = df_car[col].quantile([.75]).values[0]
    outlier_threshold = 1.5*(Q3-Q1)
    print("----------", col, "-----------")
    print("Q1", Q1)
    print("Q3", Q3)
    print("out", Q1 - outlier_threshold, Q3 + outlier_threshold)
    for idx in range(df_car[col].shape[0]):
        if df_car[col][idx] > Q3 + outlier_threshold or df_car[col][idx] < Q1 - outlier_threshold:
            print("before", df_car[col][idx])
            df_car.set_value(df_car.index[idx], col, df_car[col].iloc[idx])
            print("after", df_car[col][idx])   
    return df_car

for col in ['load', 'temp', 'rpm', 'speed']:
    df_car = clean_data_by_IQR(df_car, col)  


print("************************** Fuel Percentage to litre") 
for idx in range(df_car.shape[0]):
    df_car.set_value(df_car.index[idx], 'fuel', df_car.loc[:,'fuel'][idx]*0.4)  
    
#print("************************** Calculating Fuel Usage")     
#for idx in range(1,df_car.shape[0]):
#    if (df_car.loc[:,'fuel'][idx-1] - df_car.loc[:,'fuel'][idx]) != 0:
#        fuel_usage = (df_car.loc[:,'mileage'][idx-1] - df_car.loc[:,'mileage'][idx])/(df_car.loc[:,'fuel'][idx-1] - df_car.loc[:,'fuel'][idx])
#    else:
#        fuel_usage = 15
#    df_car.set_value(df_car.index[idx], 'fuel_usage', fuel_usage)
#df_car.set_value(df_car.index[0], 'fuel_usage', df_car.loc[:,'fuel_usage'][1])

#
print("************************** Converting Time to pd Series")
for idx in range(df_car.shape[0]):
    df_car.set_value(df_car.index[idx], "convertTime", pd.to_datetime(df_car.index[idx].format('YYYY-MM-DD HH:mm:ss')))
df_car = df_car.set_index("convertTime")

def sma_smooth_and_plot(df_car,col):
    plt.figure()
    col_plot = df_car[[col]]
    buffer_col = col_plot.copy()
#    col_plot[col+'_sma5'] = buffer_col.rolling(5).mean()
#    col_plot[col+'_sma10'] = buffer_col.rolling(10).mean()
#    col_plot[col+'_sma25'] = buffer_col.rolling(25).mean()
#    col_plot[col+'_sma50'] = buffer_col.rolling(50).mean()
#    col_plot = col_plot.drop(columns=[col])
    col_plot.plot(figsize=(15,8), linewidth=3)
    plt.xlabel('Date', fontsize=10);
    plt.title(col)
    

for col in df_car.keys():
    sma_smooth_and_plot(df_car, col)

df_car.to_csv('df_car.csv')