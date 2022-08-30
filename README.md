# spring-boot-load-balancer-k8s

spring-boot-load-balancer-k8s is Spring boot Microservice + Kubernetes project.
Here we will see how Kubernetes automatic load balancing if we have multiple replica in our deployment.

## Installation

I have already pushed this service image on docker hub [image link](https://hub.docker.com/repository/docker/iamgovindthakur/spring-boot-load-balancer-k8s) you can use that or you can create your own image with my source code.

```bash
#Creating docker iamge
docker build -t iamgovindthakur/spring-boot-load-balancer-k8s .

#Pushing it to docker hub repository
docker push  iamgovindthakur/spring-boot-load-balancer-k8s
```

## Deployment Yaml File

```bash
apiVersion: apps/v1

kind: Deployment

metadata:
  name: spring-boot-load-balancer-k8s-deployment

  labels:
    app: spring-boot-load-balancer-k8s

spec:
  replicas: 3

  selector:
    matchLabels:
      app: spring-boot-load-balancer-k8s

  template:
    metadata:
      labels:
        app: spring-boot-load-balancer-k8s

    spec:
      containers:
        - name: spring-boot-load-balancer-k8s

          image: iamgovindthakur/spring-boot-load-balancer-k8s:latest
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: spring-boot-load-balancer-k8s-service
spec:
  type: NodePort
  selector:
    app: spring-boot-load-balancer-k8s
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
      nodePort: 30100

```

## Usage

Create A yaml file with above code or go inside deployment-yaml folder and hit below command.

```bash
kubectl apply -f spring-boot-load-balancer-k8s.yml
```
Wait for some time to boot up the pods. After try hitting the below command in your terminal

Try the below command to get deployment details

```bash
kubectl get all -o wide
```

You will be able to see your deployment details like below
```bash
H:\SpringBootS3\spring-boot-load-balancer\deployment-yaml>kubectl get all -o wide
NAME                                                          READY   STATUS    RESTARTS   AGE     IP           NODE             NOMINATED NODE   READINESS GATES
pod/spring-boot-load-balancer-k8s-deployment-dd98965f-5sw69   1/1     Running   0          2m22s   10.1.0.129   docker-desktop   <none>           <none>
pod/spring-boot-load-balancer-k8s-deployment-dd98965f-99gfv   1/1     Running   0          2m23s   10.1.0.130   docker-desktop   <none>           <none>
pod/spring-boot-load-balancer-k8s-deployment-dd98965f-jhclf   1/1     Running   0          2m22s   10.1.0.131   docker-desktop   <none>           <none>

NAME                                            TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)          AGE     SELECTOR
service/kubernetes                              ClusterIP   10.96.0.1     <none>        443/TCP          3d11h   <none>
service/spring-boot-load-balancer-k8s-service   NodePort    10.96.29.13   <none>        8080:30100/TCP   4s      app=spring-boot-load-balancer-k8s

NAME                                                       READY   UP-TO-DATE   AVAILABLE   AGE     CONTAINERS                      IMAGES                                                 SELECTOR
deployment.apps/spring-boot-load-balancer-k8s-deployment   3/3     3            3           2m24s   spring-boot-load-balancer-k8s   iamgovindthakur/spring-boot-load-balancer-k8s:latest   app=spring-boot-load-balancer-k8s

NAME                                                                DESIRED   CURRENT   READY   AGE     CONTAINERS                      IMAGES                                                 SELECTOR
replicaset.apps/spring-boot-load-balancer-k8s-deployment-dd98965f   3         3         3       2m24s   spring-boot-load-balancer-k8s   iamgovindthakur/spring-boot-load-balancer-k8s:latest   app=spring-boot-load-balancer-k8s,pod-template-hash=dd98965f
```

Now check If our load balancing is working or not. Hit the below command 10-15 times in your terminal.

```bash
curl -X GET "http://localhost:30100/host-details" -H "accept: */*"
```
You will notice it will do automatic load balancing and you will get different Current IP in response which is nothing but the pods local IP.


## Screenshot

![alt text](https://raw.githubusercontent.com/iamgovindthakur/spring-boot-load-balancer-k8s/main/screenshots/spring-boot-load-balancer-k8s.png)

