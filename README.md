# Jobless
[简介](#简介) | [功能](#功能) | [构建](#构建) | [部署](#部署)

## 简介
```JobWorker``` & ```Kubeless```

#### 功能
* 将 ```Kubeless``` 的 ```Function``` 作为 ```JobWorker```。

#### 技术栈
* Spring Boot
* OAuth2.0

## 构建
```shell
mvn package
```

## 部署
### Helm 部署

[Helm](https://helm.sh) must be installed to use the charts.  Please refer to
Helm's [documentation](https://helm.sh/docs) to get started.

Once Helm has been set up correctly, add the repo as follows:

    helm repo add jobless https://dustlight-cn.github.io/jobless

If you had already added this repo earlier, run `helm repo update` to retrieve
the latest versions of the packages.  You can then run `helm search repo
fun` to see the charts.

To install the Jobless chart:

    helm install my-jobless jobless/jobless

To uninstall the chart:

    helm delete my-jobless
