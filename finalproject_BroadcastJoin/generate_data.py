import random

def generate_data(employee_num, department_num):
    list_employeename=[]
    list_managername=[]
    for i in range(employee_num):
        if i % 2 == 1:
            name=random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ')+random.choice('abcdefghijklmnopqrstuvwxyz')+random.choice('abcdefghijklmnopqrstuvwxyz')
        else:
            name=random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ')+random.choice('abcdefghijklmnopqrstuvwxyz')
        list_employeename.append(name)

    for i in range(department_num):
        if i % 2 == 1:
            name=random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ')+random.choice('abcdefghijklmnopqrstuvwxyz')+random.choice('abcdefghijklmnopqrstuvwxyz')
        else:
            name=random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ')+random.choice('abcdefghijklmnopqrstuvwxyz')
        list_managername.append(name)

    with open('input/emp', 'a') as f:
        count = 0
        for name in list_employeename:
            f.write(name + '\t' + str(count) + '\t' + str(random.randint(0, department_num - 1)) + '\n')
            count = count + 1
        f.close()


    with open('input/dept', 'a') as f:
        count = 0
        for name in list_managername:
            f.write(str(count) + '\t' + name + '\n')
            count = count + 1
        f.close()
        
if __name__ == '__main__':
    generate_data(100000, 100)
