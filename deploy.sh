set -e


# ====== 變數設定 ======
AWS_REGION="us-east-1"
AWS_ACCOUNT_ID="590184111590"
REPO_USER="user-service"
REPO_ORDER="order-service"
REPO_MARKET="market-data-service"
EKS_CLUSTER_NAME="microservices-cluster"


# ====== 登入 AWS ECR ======
echo "Logging in to AWS ECR..."
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com


# ====== Build & Push User Service ======
echo "Building & Pushing User Service..."
docker build -t ${REPO_USER}:latest ./user-service
docker tag ${REPO_USER}:latest ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_USER}:latest
docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_USER}:latest


# ====== Build & Push Order Service ======
echo "Building & Pushing Order Service..."
docker build -t ${REPO_ORDER}:latest ./order-service
docker tag ${REPO_ORDER}:latest ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_ORDER}:latest
docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_ORDER}:latest


# ====== Build & Push Market Data Service ======
echo "Building & Pushing Market Data Service..."
docker build -t ${REPO_MARKET}:latest ./market-data-service
docker tag ${REPO_MARKET}:latest ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_MARKET}:latest
docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_MARKET}:latest


# ====== 設定 kubectl 連到 EKS ======
echo "Updating kubeconfig..."
aws eks update-kubeconfig --region ${AWS_REGION} --name ${EKS_CLUSTER_NAME}


# ====== Kubernetes 部署 ======
echo "Applying Kubernetes resources..."
kubectl apply -f k8s/microservices.yaml


echo "✅ Deployment Completed Successfully!"
