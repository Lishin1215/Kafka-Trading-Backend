// 設定 AWS 提供者，指定區域為 us-east-1
provider "aws" {
  region = "eu-west-2" # 修改為您的 AWS 區域
}

// 建立 EKS 集群，名稱為 "microservices-cluster"
resource "aws_eks_cluster" "eks_cluster" {
  name     = "microservices-cluster" // 集群名稱
  role_arn = aws_iam_role.eks_role.arn // 指定 IAM 角色的 ARN

  // 配置 VPC 子網，用於集群的網路設置
  vpc_config {
    subnet_ids = aws_subnet.eks_subnets[*].id // 使用之前定義的子網 ID
  }
}

// 建立 IAM 角色，供 EKS 集群使用
resource "aws_iam_role" "eks_role" {
  name = "eks-role" // IAM 角色名稱

  // 定義角色的信任策略，允許 EKS 使用此角色
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Principal = {
          Service = "eks.amazonaws.com" // 允許 EKS 使用此角色
        }
      }
    ]
  })
}

// 將 AmazonEKSClusterPolicy 附加到 EKS 角色
resource "aws_iam_role_policy_attachment" "eks_policy_attachment" {
  role       = aws_iam_role.eks_role.name // 指定角色名稱
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy" // 附加的策略 ARN
}

// 建立 EKS 節點組，用於管理工作節點
resource "aws_eks_node_group" "eks_nodes" {
  cluster_name    = aws_eks_cluster.eks_cluster.name // 所屬的 EKS 集群名稱
  node_group_name = "eks-node-group" // 節點組名稱
  node_role_arn   = aws_iam_role.eks_node_role.arn // 指定節點的 IAM 角色
  subnet_ids      = aws_subnet.eks_subnets[*].id // 使用之前定義的子網 ID

  // 節點組的縮放配置
  scaling_config {
    desired_size = 2 // 期望的節點數量
    max_size     = 3 // 最大節點數量
    min_size     = 1 // 最小節點數量
  }
}

// 建立 IAM 角色，供 EKS 節點使用
resource "aws_iam_role" "eks_node_role" {
  name = "eks-node-role" // IAM 角色名稱

  // 定義角色的信任策略，允許 EC2 使用此角色
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com" // 允許 EC2 使用此角色
        }
      }
    ]
  })
}

// 將 AmazonEKSWorkerNodePolicy 附加到 EKS 節點角色
resource "aws_iam_role_policy_attachment" "eks_node_policy_attachment" {
  role       = aws_iam_role.eks_node_role.name // 指定角色名稱
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy" // 附加的策略 ARN
}

// 建立 VPC，用於 EKS 集群的網路
resource "aws_vpc" "eks_vpc" {
  cidr_block = "10.0.0.0/16" // VPC 的 CIDR 範圍
}

// 建立子網，供 EKS 集群使用
resource "aws_subnet" "eks_subnets" {
  count = 2 // 建立兩個子網

  vpc_id                  = aws_vpc.eks_vpc.id // 所屬的 VPC ID
  cidr_block              = cidrsubnet(aws_vpc.eks_vpc.cidr_block, 8, count.index) // 子網的 CIDR 範圍
  availability_zone       = data.aws_availability_zones.available.names[count.index] // 可用區域
  map_public_ip_on_launch = true // 啟用公有 IP
}

// 獲取可用的 AWS 可用區域
data "aws_availability_zones" "available" {}

// 輸出 EKS 集群的端點 URL
output "eks_cluster_endpoint" {
  value = aws_eks_cluster.eks_cluster.endpoint // 集群的端點 URL
}

// 輸出 EKS 集群的名稱
output "eks_cluster_name" {
  value = aws_eks_cluster.eks_cluster.name // 集群名稱
}